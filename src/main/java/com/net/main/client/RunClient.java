package com.net.main.client;

import com.game.utils.Log;
import com.net.handler.SocketClientSampleHandler;
import com.net.main.initializer.SocketClientInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *
 * </pre>
 *
 * @author yuxuan
 * @time 2020-03-06 15:24
 */
public class RunClient {

    public static void main(String[] args) {
        BaseSocketClient client = (BaseSocketClient) BaseNettyClient.createInstance(BaseSocketClient.class, "SampleSocketServer",
                "192.168.1.49", 6821, 1, new SocketClientInitializer(new SocketClientSampleHandler()));
        for (int i = 0; i < 1; i++)
            testSend1(client);
    }


    private static String headStr = "{\"code\":32767,\"rid\":112233}";
    private static String bodyStr = "{\"c\":1,\"msg\":\"123哈哈哈~\"}";
    private static byte[] headBytes = headStr.getBytes(StandardCharsets.UTF_8);
    private static byte[] bodyBytes = bodyStr.getBytes(StandardCharsets.UTF_8);

    public static void testSend1(BaseSocketClient client) {
        Channel channel = client.selectChannel();
        ByteBufAllocator allocator = channel.alloc();
        System.err.println(allocator);
        int len = 2 + headBytes.length + bodyBytes.length;
        ByteBuf buf = allocator.buffer(len);
        buf.writeShort(len);
        buf.writeShort(headBytes.length);
        buf.writeBytes(headBytes);
        buf.writeBytes(bodyBytes);
        channel.writeAndFlush(buf);
        Log.info("test send1,head:" + new String(headBytes) + ",body:" + new String(bodyBytes));
    }

    public static void testSendStr(BaseSocketClient client) {
        Channel channel = client.selectChannel();
        byte[] bytes = (bodyStr + Log.NEW_LINE).getBytes();
        ByteBufAllocator allocator = channel.alloc();
        ByteBuf byteBuf = allocator.buffer(bytes.length);
        byteBuf.writeBytes(bytes);
        channel.writeAndFlush(byteBuf.copy());
        channel.writeAndFlush(byteBuf.copy());
        channel.writeAndFlush(byteBuf.copy());
        channel.writeAndFlush(byteBuf.copy());
        channel.writeAndFlush(byteBuf.copy());
        channel.writeAndFlush(byteBuf.copy());
        Log.info("test testSendStr,body:" + bodyStr);
    }
}
