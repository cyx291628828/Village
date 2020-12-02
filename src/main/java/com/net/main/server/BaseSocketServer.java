package com.net.main.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.socket.SocketChannel;

/**
 * <pre>
 *
 * </pre>
 *
 * @author yuxuan
 * @time 2020-03-06 10:51
 */
public class BaseSocketServer extends BaseNettyServer {

    public BaseSocketServer(int port, int workThreadCount, ChannelInitializer<SocketChannel> serverInitializer) {
        super(port, workThreadCount, serverInitializer);
    }

    @Override
    protected void initParam(ServerBootstrap b) {
        // bossGroup同一时间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
        b.option(ChannelOption.SO_BACKLOG, 1024);
        // 不管数据包大小，不组合包，直接发送
        b.childOption(ChannelOption.TCP_NODELAY, true);
        // 若无数据传输时，2小时一次探测包，判断连接是否仍然正常
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 接收区缓存大小(32K=短整型最大值)
        b.option(ChannelOption.SO_RCVBUF, 16 * 1024);
        // 接收区缓存大小
        b.childOption(ChannelOption.SO_RCVBUF, 16 * 1024);
        // 发送区缓存大小
        b.childOption(ChannelOption.SO_SNDBUF, 16 * 1024);
        // 使用内存池ByteBuf，且偏向于DirectBuff
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        // 使用内存池ByteBuf，且偏向于DirectBuff
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        // 接收区缓存自动调节大小，默认DirectBuff
        b.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
        // 写队列缓冲区字节数大于高水位时，Channel.isWritable()返回false，当降到低水位以下时，重新返回true(每个channel独立计算)
        b.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 512, 1024 * 1024));
    }

}
