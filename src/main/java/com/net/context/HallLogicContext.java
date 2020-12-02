package com.net.context;

import com.game.executor.task.BaseTask;
import com.game.player.HallPlayer;
import com.game.player.HallPlayerMgr;
import com.game.utils.JsonUtil;
import com.game.utils.Log;
import com.net.code.AnnCode;
import com.net.code.MethodAndAnnCode;
import com.net.context.base.BaseLogicContext;
import com.net.context.base.ILogicContext;
import io.netty.channel.Channel;

/**
 * <pre>
 * 大厅逻辑处理上下文
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-03 16:54
 */
public class HallLogicContext extends BaseLogicContext {
    private HallPlayer hallPlayer;

    public HallLogicContext(Channel channel) {
        super(channel);
        if (userId != 0) {
            this.hallPlayer = HallPlayerMgr.INSTANCE.get(userId);
        }
    }

    @Override
    public HallPlayer getHallPlayer() {
        return hallPlayer;
    }

    @Override
    public void handleRequest(MethodAndAnnCode man, final StringBuilder sbLog) throws Exception {
        final AnnCode annCode = man.annCode;
        if (hallPlayer != null) {
            final ILogicContext self = this;
            HallPlayerMgr.INSTANCE.submitTaskS(hallPlayer, new BaseTask() {
                @Override
                protected void exec() throws Exception {
                    man.method.invoke(man.logic, self);
                    toLog(sbLog);
                }

                @Override
                protected String getName() {
                    return "HandleRequest-" + userId + "-" + annCode.reqCode();
                }
            });
        } else {
            man.method.invoke(man.logic, this);
            toLog(sbLog);
        }
    }

    @Override
    public void handleResponse(MethodAndAnnCode man, final StringBuilder sbLog) throws Exception {
        final AnnCode annCode = man.annCode;
        if (annCode.resCode() == 0 || annCode.resClazz() == Class.class) {
            return;
        }
        if (hallPlayer != null) {
            HallPlayerMgr.INSTANCE.submitTaskS(hallPlayer, new BaseTask() {
                @Override
                protected void exec() throws Exception {
                    HallPlayerMgr.INSTANCE.sendClient(userId, annCode.resCode(), resData);
                    if (sbLog != null) {
                        sbLog.append(",sendClient:");
                        sbLog.append(JsonUtil.stringify(resData));
                    }
                }

                @Override
                protected String getName() {
                    return "HandleResponse-" + userId + "-" + annCode.reqCode() + "-" + annCode.resCode();
                }
            });
        } else {
            HallPlayerMgr.INSTANCE.sendClient(userId, annCode.resCode(), resData);
            if (sbLog != null) {
                sbLog.append(",sendClient:");
                sbLog.append(JsonUtil.stringify(resData));
            }
        }
    }

    @Override
    public void handleLog(MethodAndAnnCode man, final StringBuilder sbLog) {
        AnnCode annCode = man.annCode;
        if (hallPlayer != null) {
            HallPlayerMgr.INSTANCE.submitTaskS(hallPlayer, new BaseTask() {
                @Override
                protected void exec() throws Exception {
                    if (sbLog != null) {
                        Log.info(sbLog.toString());
                    }
                }

                @Override
                protected String getName() {
                    return "HandleLog-" + userId + "-" + annCode.reqCode() + "-" + annCode.resCode();
                }
            });
        } else {
            if (sbLog != null) {
                Log.info(sbLog.toString());
            }
        }

    }

}
