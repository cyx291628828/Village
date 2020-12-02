package com.game.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Arrays;

/**
 * <pre>
 * ByteBuf工具
 * </pre>
 *
 * @author yuxuan
 * @time 2020-03-23 16:21
 */
public class ByteBufUtil {

    /**
     * <pre>
     * 连接是否有效
     * </pre>
     */
    public static boolean isChannelActive(Channel channel) {
        return channel != null && channel.isOpen() && channel.isActive();
    }


    /**
     * <pre>
     * 释放内存
     * </pre>
     */
    public static void releaseBuf(final Channel channel, final ByteBuf buf) {
        if (buf == null) {
            return;
        }
        if (!isChannelActive(channel)) {
            release0(buf);
            return;
        }
        channel.eventLoop().execute(() ->
                release0(buf)
        );
    }

    private static void release0(ByteBuf buf) {
        while (buf.refCnt() > 0) {
            try {
                buf.release();
            } catch (Exception e) {
                Log.error("buf_release_error", e);
            }
        }
    }

    public static boolean checkLength(short code, byte[] bytes) {
        if (bytes == null) {
            return false;
        }
        // |整包长度2字节(不包含自身)|标识位1字节|协议号2字节|包体数据|
        int len = 2 + 2 + 1 + bytes.length;
        if (len > Short.MAX_VALUE) {
            Log.fatal("packet length over limit,len:" + len + ",code:" + code);
            return false;
        }
        return true;
    }

    public static void printBytes(String name, ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        System.err.println("------------------- " + name + ":" + Arrays.toString(bytes));
    }


}
