package com.net.handler;

import com.game.utils.Log;
import com.net.channel.ChannelMgr;
import com.net.code.CodeExecutor;
import com.net.packet.PacketHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * <pre>
 * 大厅Handler(临时版本)
 * </pre>
 *
 * @author yuxuan
 * @time 2020-03-10 20:13
 */
@ChannelHandler.Sharable
public class VillageServerHandler extends BaseSocketServerHandler {

    public final static AttributeKey<Long> CHANNEL_UID = AttributeKey.valueOf("channel_uid");

    public VillageServerHandler() {
        super(true);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) {
        Long userId = ctx.channel().attr(CHANNEL_UID).get();
        if (userId != null) {
            ChannelMgr.INSTANCE.changeWritable(userId, ctx.channel().isWritable());
        }
    }

    @Override
    protected void handleDisconnect(ChannelHandlerContext ctx) {
        Long userId = ctx.channel().attr(CHANNEL_UID).get();
        if (userId != null) {
            Log.info("UserDisconnect,userId:" + userId + " " + ctx.channel());
            // 移除链接
            ChannelMgr.INSTANCE.remove(userId);
        }
    }

    @Override
    protected void handleClientRequest(ChannelHandlerContext ctx, ByteBuf buf) {
        short code = buf.readShort();
        if (code < 1) {
            throw new IllegalStateException("reqCode is error,reqCode:" + code);
        }
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        ChannelMgr.INSTANCE.getOpsMonitor().addTotalRCt(1);
        CodeExecutor.INSTANCE.handle(ctx.channel(), code, bytes);
    }

    @Override
    protected void handleClientResponse(ChannelHandlerContext ctx, ByteBuf buf) {
    }

    @Override
    protected void onMessage(ChannelHandlerContext ctx, ByteBuf buf, PacketHead head) {
    }


}
