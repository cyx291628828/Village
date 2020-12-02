package com.net.main.server;

import com.game.utils.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseNettyServer {


    protected Channel ch;
    protected boolean start;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workerGroup;
    private final AtomicInteger threadIdx = new AtomicInteger();

    public BaseNettyServer(int port, int workThreadCount, ChannelInitializer<SocketChannel> serverInitializer) {
        init(port, workThreadCount, serverInitializer);
    }

    private final void init(int port, int workThreadCount, ChannelInitializer<SocketChannel> serverInitializer) {
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

        final String name = this.getClass().getSimpleName();

        try {
            ServerBootstrap b = new ServerBootstrap();
            if (useEpoll()) {
                bossGroup = new EpollEventLoopGroup(1, (r) -> {

                    return new FastThreadLocalThread(r, name + "-BossThread");
                });
                workerGroup = new EpollEventLoopGroup(workThreadCount, (r) -> {

                    return new FastThreadLocalThread(r, name + "-WorkThread-" + threadIdx.incrementAndGet());
                });
                b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class).childHandler(serverInitializer);
            } else {
                bossGroup = new NioEventLoopGroup(1, (r) -> {

                    return new FastThreadLocalThread(r, name + "-BossThread");
                });
                workerGroup = new NioEventLoopGroup(workThreadCount, (r) -> {

                    return new FastThreadLocalThread(r, name + "-WorkThread-" + threadIdx.incrementAndGet());
                });
                b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(serverInitializer);
            }

            initParam(b);
            ch = b.bind(port).sync().channel();
            Log.info(name + " listen：" + port);
            start = true;
        } catch (Throwable e) {
            Log.error("NettyServer start error,port：" + port, e);
            System.exit(-1);
        }
    }

    private boolean useEpoll() {
        return Epoll.isAvailable();
    }


    /**
     * <pre>
     * 设置服务器参数
     * </pre>
     */
    protected abstract void initParam(ServerBootstrap b);

    /**
     * <pre>
     * 停止服务器
     * </pre>
     */
    public final void close() {
        try {
            ch.close().syncUninterruptibly();
            ch.closeFuture().syncUninterruptibly();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } catch (Exception e) {
            Log.error("NettyServer close error:", e);
        }
    }

    /**
     * <pre>
     * 创建服务器
     * </pre>
     */
    public static BaseNettyServer createInstance(Class<?> clazz, int port, int workThreadCount, ChannelInitializer<SocketChannel> serverInitializer) throws Exception {
        Constructor<?> constructor = clazz.getConstructor(int.class, int.class, ChannelInitializer.class);
        if (constructor == null) {
            Log.error("构造方法参数错误：" + clazz.getName());
            return null;
        }
        constructor.setAccessible(true);
        BaseNettyServer server = (BaseNettyServer) constructor.newInstance(port, workThreadCount, serverInitializer);
        if (!server.isStart()) {
            return null;
        }
        return server;
    }

    public final boolean isStart() {
        return start;
    }

}
