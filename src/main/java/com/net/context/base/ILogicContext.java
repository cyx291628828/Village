package com.net.context.base;

import com.game.player.HallPlayer;
import com.net.code.AnnCode;
import com.net.code.MethodAndAnnCode;
import io.netty.channel.Channel;

/**
 * <pre>
 * 逻辑上下文接口
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-17 14:59
 */
public interface ILogicContext {
    long getUserId();

    void setReqBytes(byte[] reqBytes);

    <T> T getReqData();

    Object getResData();

    void setResData(Object resData);

    Channel getChannel();

    void setChannel(Channel channel);

    AnnCode getAnnCode();

    void setAnnCode(AnnCode annCode);

    void toLog(StringBuilder sbLog);

    HallPlayer getHallPlayer();

    void handleRequest(MethodAndAnnCode man, StringBuilder sbLog) throws Exception;

    void handleResponse(MethodAndAnnCode man, StringBuilder sbLog) throws Exception;

    void handleLog(MethodAndAnnCode man, StringBuilder sbLog);
}
