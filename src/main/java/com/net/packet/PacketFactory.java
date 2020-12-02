package com.net.packet;

import com.game.consts.PacketType;
import com.game.utils.ByteBufUtil;
import com.game.utils.Log;
import com.game.utils.serialize.ISerialize;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.List;


/**
 * <pre>
 * 数据包工厂
 * </pre>
 *
 * @author yuxuan
 * @time 2020-03-16 18:16
 */
public class PacketFactory {

    public static PacketHead buildHead(short code) {
        return new PacketHead(code, 0);
    }

    public static ByteBuf buildPacket(Channel channel, short code, Object body) {
        PacketHead packetHead = buildHead(code);
        return buildPacket(channel, packetHead, body);
    }


    public static ByteBuf buildPacket(Channel channel, PacketHead head, Object body) {
        byte[] headBytes = ISerialize.INSTANCE.encode(head);
        byte[] bodyBytes = ISerialize.INSTANCE.encode(body);
        // |整包长度2字节(不包含自身)|标识位1字节|头部长度2字节|头部数据|包体数据|
        int len = 2 + 1 + 2 + headBytes.length + bodyBytes.length;
        if (len > Short.MAX_VALUE) {
            Log.fatal("packet length over limit,len:" + len + head.basicDesc());
            return null;
        }
        ByteBuf buf;
        if (channel != null) {
            buf = channel.alloc().buffer(len);
        } else {
            buf = Unpooled.buffer(len);
        }
        buf.writeShort(len - 2);
        buf.writeByte(PacketType.SERVER_PACKET);
        buf.writeShort(headBytes.length);
        buf.writeBytes(headBytes);
        buf.writeBytes(bodyBytes);
        return buf;
    }


    public static ByteBuf composePacket(Channel channel, PacketHead head, ByteBuf bodyBuf) {
        byte[] headBytes = ISerialize.INSTANCE.encode(head);
        // |整包长度2字节(不包含自身)|标识位1字节|头部长度2字节|头部数据|包体数据|
        int len = 2 + 1 + 2 + headBytes.length + bodyBuf.readableBytes();
        if (len > Short.MAX_VALUE) {
            Log.fatal("packet lenght over limit,len:" + len + head.basicDesc());
            return null;
        }
        ByteBuf headBuf = channel.alloc().buffer(len);
        headBuf.writeShort(len - 2);
        headBuf.writeByte(PacketType.SERVER_PACKET);
        headBuf.writeShort(headBytes.length);
        headBuf.writeBytes(headBytes);

        return new CompositeByteBuf(channel.alloc(), true, 2, headBuf, bodyBuf);
    }


    public static ByteBuf clientPacket(Channel channel, short code, ByteBuf bodyBuf) {
        if (bodyBuf == null || channel == null) {
            return null;
        }
        // |整包长度2字节(不包含自身)|标识位1字节|协议号2字节|包体数据|
        int len = 2 + 1 + 2 + bodyBuf.readableBytes();
        if (len > Short.MAX_VALUE) {
            Log.fatal("packet lenght over limit,len:" + len + ",code:" + code);
            return null;
        }
        ByteBuf headBuf = channel.alloc().buffer(len);
        headBuf.writeShort(len - 2);
        headBuf.writeByte(PacketType.CLIENT_RESPONSE);
        headBuf.writeShort(code);

        return new CompositeByteBuf(channel.alloc(), true, 2, headBuf, bodyBuf);
    }

    public static ByteBuf clientPacket(Channel channel, short code, byte[] bytes) {
        if (bytes == null || channel == null) {
            return null;
        }
        if (!ByteBufUtil.checkLength(code, bytes)) {
            return null;
        }
        // |整包长度2字节(不包含自身)|标识位1字节|协议号2字节|包体数据|
        int len = 2 + 2 + 1 + bytes.length;

        ByteBuf resBuf;
        ByteBufAllocator allocator = channel.alloc();
        if (allocator != null) {
            resBuf = allocator.buffer(len);
        } else {
            resBuf = Unpooled.buffer(len);
        }
        resBuf.writeShort(len - 2);
        resBuf.writeByte(PacketType.CLIENT_RESPONSE);
        resBuf.writeShort(code);
        resBuf.writeBytes(bytes);
        return resBuf;
    }

    public static <E extends PacketItem> ByteBuf clientPackets(Channel channel, List<E> items) {
        if (items == null || channel == null) {
            return null;
        }
        int totalLen = 0;
        for (PacketItem item : items) {
            totalLen += item.getBytes().length;
            totalLen += 2;
            totalLen += 2;
            totalLen += 1;
        }

        ByteBuf resBuf;
        ByteBufAllocator allocator = channel.alloc();
        if (allocator != null) {
            resBuf = allocator.buffer(totalLen);
        } else {
            resBuf = Unpooled.buffer(totalLen);
        }

        for (PacketItem item : items) {
            byte[] bytes = item.getBytes();
            // |整包长度2字节(不包含自身)|标识位1字节|协议号2字节|包体数据|
            int len = 2 + 1 + bytes.length;
            resBuf.writeShort(len);
            resBuf.writeByte(PacketType.CLIENT_RESPONSE);
            resBuf.writeShort(item.getCode());
            resBuf.writeBytes(bytes);
        }
        return resBuf;
    }

    /**
     * <pre>
     * 客户端包:整包长度2字节(不包含自身)|标识位1字节|协议号2字节|包体数据|
     * </pre>
     */
    public static ByteBuf clientPacket(Channel channel, short code, Object data) {
        if (data == null || channel == null) {
            return null;
        }
        byte[] resBytes = ISerialize.INSTANCE.encode(data);
        if (!ByteBufUtil.checkLength(code, resBytes)) {
            return null;
        }
        int len = 2 + 1 + 2 + resBytes.length;
        if (len > Short.MAX_VALUE) {
            Log.fatal("packet lenght over limit,len:" + len + ",code:" + code);
            return null;
        }
        ByteBuf resBuf;
        ByteBufAllocator allocator = channel.alloc();
        if (allocator != null) {
            resBuf = allocator.buffer(len);
        } else {
            resBuf = Unpooled.buffer(len);
        }

        resBuf.writeShort(len - 2);
        resBuf.writeByte(PacketType.CLIENT_RESPONSE);
        resBuf.writeShort(code);
        resBuf.writeBytes(resBytes);
        return resBuf;
    }

}
