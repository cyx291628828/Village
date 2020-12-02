package com.net.context.base;

import com.game.utils.JsonUtil;
import com.game.utils.serialize.ISerialize;
import com.net.code.AnnCode;
import com.net.handler.VillageServerHandler;
import io.netty.channel.Channel;

/**
 * <pre>
 * 逻辑处理上下文
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-03 16:54
 */
public abstract class BaseLogicContext implements ILogicContext {
    protected long userId;
    private Channel channel;
    private AnnCode annCode;
    private Object reqData;
    private byte[] reqBytes;
    protected Object resData;

    public BaseLogicContext(Channel channel) {
        this.channel = channel;
        Long userId = channel.attr(VillageServerHandler.CHANNEL_UID).get();
        if (userId != null) {
            this.userId = userId;
        }
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setReqBytes(byte[] reqBytes) {
        this.reqBytes = reqBytes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getReqData() {
        if (this.reqData != null) {
            return (T) this.reqData;
        }
        this.reqData = ISerialize.INSTANCE.decode(this.reqBytes, annCode.reqClazz());
        return (T) reqData;
    }

    @Override
    public Object getResData() {
        return resData;
    }

    @Override
    public void setResData(Object resData) {
        this.resData = resData;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public AnnCode getAnnCode() {
        return annCode;
    }

    @Override
    public void setAnnCode(AnnCode annCode) {
        this.annCode = annCode;
    }

    @Override
    public void toLog(StringBuilder sbLog) {
        if (sbLog != null) {
            sbLog.append(",code:");
            sbLog.append(annCode.reqCode());
            sbLog.append(",uid:");
            sbLog.append(userId);
            sbLog.append(",req:");
            sbLog.append(reqData.getClass().getSimpleName());
            sbLog.append(JsonUtil.stringify(reqData));
        }
    }

    @Override
    public String toString() {
        return "BaseLogicContext{" +
                "userId=" + userId +
                ", code=" + annCode.reqCode() +
                ", reqData=" + reqData +
                ", resData=" + resData +
                '}';
    }

}
