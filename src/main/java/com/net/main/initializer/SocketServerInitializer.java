package com.net.main.initializer;

import com.game.utils.Log;
import com.net.handler.BaseSocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private BaseSocketServerHandler baseSocketServerHandler;
    private StringDecoder stringDecoder = new StringDecoder();

    public SocketServerInitializer(BaseSocketServerHandler baseSocketServerHandler) {
        Log.debug(this.getClass().getSimpleName() + " Created~");
        this.baseSocketServerHandler = baseSocketServerHandler;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 第12位表示整包的长度,34位表示包描述信息长度,后续为描述信息字节,以及逻辑数据字节
        pipeline.addLast(new LengthFieldBasedFrameDecoder
                (Short.MAX_VALUE, 0, 2, 0, 0, false));

//        pipeline.addLast(new DelimiterBasedFrameDecoder(256, Unpooled.wrappedBuffer(Log.NEW_LINE.getBytes())));
//        pipeline.addLast(stringDecoder);
        pipeline.addLast(baseSocketServerHandler);
    }
}
