package com.net.handler;

import com.game.utils.Log;
import com.net.packet.PacketHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * <pre>
 *
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-26 16:24
 */
@ChannelHandler.Sharable
public class SocketClientSampleHandler extends BaseSocketClientHandler {


    @Override
    protected void onMessage(ChannelHandlerContext ctx, ByteBuf buf, PacketHead head) {
        short totalLen = buf.readShort();
        short headLen = buf.readShort();
        byte[] headBytes = new byte[headLen];
        buf.readBytes(headBytes);
        int bodyLen = totalLen - headLen - 2;
        if (bodyLen != buf.readableBytes()) {
            Log.error(thisName + "onMessage pkg len error,bodyLen：" + bodyLen + ",leftLen:" + buf.readableBytes());
            return;
        }
        byte[] bodyBytes = new byte[bodyLen];
        buf.readBytes(bodyBytes);
        Log.info(thisName + "onMessage,headLen:" + headBytes.length + ",bodyLen:" + bodyBytes.length);

        onMsg1(headBytes, bodyBytes);
    }

    private void onMsg1(byte[] headBytes, byte[] bodyBytes) {

        Log.info(thisName + "onMessage,head:" + new String(headBytes) + ",body:" + new String(bodyBytes));
    }

}
