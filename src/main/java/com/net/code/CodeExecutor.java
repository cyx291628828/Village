package com.net.code;

import com.game.exception.CodeErrorException;
import com.game.utils.ClassUtil;
import com.game.utils.Log;
import com.net.code.codes.CodeFilters;
import com.net.code.codes.HallCodes;
import com.net.context.HallLogicContext;
import com.net.context.base.ILogicContext;
import io.netty.channel.Channel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 协议执行器(临时版本)
 * </pre>
 *
 * @author yuxuan
 */
public class CodeExecutor {

    public final static CodeExecutor INSTANCE = new CodeExecutor();

    /**
     * 逻辑方法缓存
     */
    private final Map<Short, MethodAndAnnCode> methods = new ConcurrentHashMap<>();

    private CodeExecutor() {
    }

    /**
     * <pre>
     * 初始化方法
     * </pre>
     */
    public final void init(Class<?> location) throws Exception {
        Set<Class<?>> clazzs = ClassUtil.getClasses(location.getPackage(), AnnLogic.class);
//        Log.error("---------------clazzs:" + JsonUtil.stringify(clazzs));
        for (Class<?> clazz : clazzs) {
            Field logicField = clazz.getDeclaredField("INSTANCE");
            Object logic = logicField.get(null);
            Method[] ms = clazz.getDeclaredMethods();
            for (int i = 0, len = ms.length; i < len; i++) {
                Method m = ms[i];
                AnnCode annCode = m.getDeclaredAnnotation(AnnCode.class);
                if (annCode != null) {
                    MethodAndAnnCode preM = methods.get(annCode.reqCode());
                    String name = m.getName();
                    if (preM != null) {
                        Log.error(clazz.getSimpleName() + " duplicated method found,pre：" + preM.method.getName() + ",new:" + name + ",reqCode:" + annCode.reqCode());
                        return;
                    }
                    methods.put(annCode.reqCode(), new MethodAndAnnCode(logic, m, annCode));
                }
            }
        }
        Log.info("CodeExecutor init sucess,count：" + methods.size());
    }

    private ILogicContext getLogicContext(Channel channel, short code) {
        if (code < HallCodes.C2S_MAX) {
            return new HallLogicContext(channel);
        }
        throw new CodeErrorException("ReceivedCodeError,code:" + code + " " + channel);
    }


    /**
     * <pre>
     * 逻辑处理
     * </pre>
     */
    public final void handle(Channel channel, short code, byte[] bytes) {
        MethodAndAnnCode man = methods.get(code);
        if (man == null) {
            Log.error("code method not found,code:" + code);
            return;
        }
        ILogicContext context = null;
        try {
            StringBuilder sbLog = null;
            if (CodeFilters.INSTANCE.isReqLog(code)) {
                sbLog = new StringBuilder("received len:");
                sbLog.append(bytes.length);
            }

            context = getLogicContext(channel, code);
            // 是否已登录
            if (code != HallCodes.REGISTER && context.getUserId() == 0) {
                Log.error("CodeHandle not login,code:" + code + ",channel:" + channel);
                return;
            }
            AnnCode annCode = man.annCode;
            context.setAnnCode(annCode);
            context.setReqBytes(bytes);

            context.handleRequest(man, sbLog);
            context.handleResponse(man, sbLog);
            context.handleLog(man, sbLog);

        } catch (Exception e) {
            Log.error("code handle error,userId:" + (context != null ? context.getUserId() : "null") + ",code:" + code, e);
        }
    }


}

