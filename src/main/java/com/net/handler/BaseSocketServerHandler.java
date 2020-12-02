package com.net.handler;

import com.game.consts.PacketType;
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
 * 处理handler,
 * 字节buf消息
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:50
 */
public abstract class BaseSocketServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public BaseSocketServerHandler() {
        this(false);
    }

    public BaseSocketServerHandler(boolean autoRelease) {
        super(autoRelease);
        Log.debug(this.getClass().getSimpleName() + " Created~");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Log.debug(this.getClass().getSimpleName() + " handler被添加到channel的pipeline");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Log.debug(ctx.channel().getClass().getSimpleName() + "注册到EventLoopGroup");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.debug(ctx.channel().getClass().getSimpleName() + "准备就绪");
    }

    /**
     * <pre>
     * 接收到消息
     * </pre>
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        try {
            short tL = byteBuf.readShort();
            byte tag = byteBuf.readByte();
//            Log.debug(thisName + "channelRead0,len:" + tL + ",tag:" + tag);

            // 客户端请求包
            if (tag == PacketType.CLIENT_REQUEST) {
                handleClientRequest(ctx, byteBuf);
            }
            // 客户端返回包
            else if (tag == PacketType.CLIENT_RESPONSE) {
                handleClientResponse(ctx, byteBuf);
            }
            // 服务端包
            else if (tag == PacketType.SERVER_PACKET) {
                short hL = byteBuf.readShort();
                byte[] hB = new byte[hL];
                byteBuf.readBytes(hB);
                int lL = byteBuf.readableBytes();
                int nL = tL - 1 - 2 - hL;
                if (nL != lL) {
                    Log.error("buf len error,left:" + lL + ",need:" + nL);
                    return;
                }
                PacketHead head = ISerialize.INSTANCE.decode(hB, PacketHead.class);
//              Log.debug(thisName + "channelRead0,code:" + head.getCode() + ":" + head.getSrc() + "->" + head.getDest() + ",len:" + tL + ",headLen:" + hL + ",bodyLen:" + nL);

                if (head.getCode() < 1) {
                    return;
                }

                if (StringUtil.isEmpty(head.getQid())) {
                    return;
                }
                onMessage(ctx, byteBuf, head);
            }
        } catch (Throwable e) {
            Log.error("", e);
            ByteBufUtil.releaseBuf(ctx.channel(), byteBuf);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.debug(ctx.channel().getClass().getSimpleName() + "关闭连接");
        handleDisconnect(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.error(ctx.channel().getClass().getSimpleName() + "连接发生异常", cause);
        handleDisconnect(ctx);
    }

    protected abstract void handleDisconnect(ChannelHandlerContext ctx);

    protected abstract void onMessage(ChannelHandlerContext ctx, ByteBuf byteBuf, PacketHead head);

    protected abstract void handleClientRequest(ChannelHandlerContext ctx, ByteBuf byteBuf);

    protected abstract void handleClientResponse(ChannelHandlerContext ctx, ByteBuf byteBuf);
}
