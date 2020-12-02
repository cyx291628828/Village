package com.net.main.initializer;

import com.game.utils.Log;
import com.net.handler.BaseSocketClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class SocketClientInitializer extends ChannelInitializer<SocketChannel> {

    private BaseSocketClientHandler handler;

    public SocketClientInitializer(BaseSocketClientHandler handler) {
        Log.debug(this.getClass().getSimpleName() + " Created~");
        this.handler = handler;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 第1、2位表示整包的长度,3、4位表示包描述信息长度,后续为描述信息字节,以及逻辑数据字节
        pipeline.addLast(new LengthFieldBasedFrameDecoder
                (Short.MAX_VALUE, 0, 2, 0, 0, false));

        pipeline.addLast(handler);
    }
}
