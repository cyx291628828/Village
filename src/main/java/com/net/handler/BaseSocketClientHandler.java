package com.net.handler;


import com.game.utils.ByteBufUtil;
import com.game.utils.Log;
import com.game.utils.StringUtil;
import com.game.utils.serialize.ISerialize;
import com.net.packet.PacketHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <pre>
 * Socket客户端
 * </pre>
 *
 * @author yuxuan
 */
public abstract class BaseSocketClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    protected final String thisName;

    public BaseSocketClientHandler() {
        super(false);
        thisName = this.getClass().getSimpleName() + " ";
        Log.debug(thisName + "Created~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info(thisName + "channelInactive:" + ctx);
        handleDisconnect(ctx);
    }

    /**
     * @param ctx
     * @param cause
     * @throws Exception
     * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(ChannelHandlerContext, Throwable)
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.error(thisName + "exceptionCaught:" + ctx, cause);
        handleDisconnect(ctx);
    }

    /**
     * <pre>
     * 处理连接断开
     * </pre>
     *
     * @param ctx
     */
    protected void handleDisconnect(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.info(thisName + "channelActive:" + ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        try {
            short tL = buf.readShort();
            byte flag = buf.readByte();
            short hL = buf.readShort();
            byte[] hB = new byte[hL];
            buf.readBytes(hB);
            int lL = buf.readableBytes();
            int nL = tL - 1 - 2 - hL;
            if (nL != lL) {
                Log.error("buf len error,left:" + lL + ",need:" + nL);
                return;
            }
            PacketHead head = ISerialize.INSTANCE.decode(hB, PacketHead.class);
//            Log.debug(thisName + "channelRead0,code:" + head.getCode() + ":" + head.getSrc() + "->" + head.getDest());

            if (head.getCode() < 1) {
                return;
            }

            if (StringUtil.isEmpty(head.getQid())) {
                return;
            }
            onMessage(ctx, buf, head);
        } catch (Throwable e) {
            Log.error("", e);
        } finally {
            ByteBufUtil.releaseBuf(ctx.channel(), buf);
        }
    }


    protected abstract void onMessage(ChannelHandlerContext ctx, ByteBuf buf, PacketHead head);

}
