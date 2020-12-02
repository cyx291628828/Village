package com.net.main.client;

import com.game.utils.Log;
import com.game.utils.RandomUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * Netty客户端基类
 * </pre>
 *
 * @author yuxuan
 */
public abstract class BaseNettyClient {

    private int port;
    private String ip;
    private Bootstrap b;
    private String serverName;
    protected EventLoopGroup workerGroup;
    protected final List<Channel> chs = new ArrayList<>();
    private final AtomicInteger threadIdx = new AtomicInteger();

    public BaseNettyClient(final String serverName, String ip, int port, int workThreadCount, ChannelInitializer<SocketChannel> channelInitializer) {
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

        if (useEpoll()) {
            this.workerGroup = new EpollEventLoopGroup(workThreadCount, (r) -> {
                return new FastThreadLocalThread(r, serverName + "-ClientThread-" + threadIdx.incrementAndGet());
            });
        } else {
            this.workerGroup = new NioEventLoopGroup(workThreadCount, (r) -> {
                return new FastThreadLocalThread(r, serverName + "-ClientThread-" + threadIdx.incrementAndGet());
            });
        }
        try {
            this.ip = ip;
            this.port = port;
            this.b = new Bootstrap();
            this.serverName = serverName;
            b.group(workerGroup).channel(NioSocketChannel.class).handler(channelInitializer);
            initParam(b);
            synchronized (chs) {
                for (int i = 0; i < workThreadCount; i++) {
                    chs.add(b.connect(this.ip, this.port).sync().channel());
                }
            }
            Log.info("connect " + serverName + " succ,ip：" + ip + ",port:" + port);
//            heartBeat(); // 启动心跳
        } catch (Throwable e) {
            Log.error(this.serverName + " connect error", e);
        }
    }

    private boolean useEpoll() {
        return Epoll.isAvailable();
    }

//    private void heartBeat() {
//        PacketHead head = new PacketHead();
//        head.setSrc(SelfDescription.cluster.getAddr());
//        head.setCode(CodesEnum.RPC_HEART_BEAT.getCode());
//        RpcHeartBeatMsg heartBeatMsg = new RpcHeartBeatMsg();
//        Timer timer = new Timer("HeartBeat-TimerThread", true);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                List<Channel> channels = new ArrayList<>(chs);
//                for (int idx = 0, len = channels.size(); idx < len; idx++) {
//                    try {
//                        Channel channel = channels.get(idx);
//                        if (!isActive(channel)) {
//                            channel = reconnect(idx);
//                        }
//                        heartBeatMsg.setSecs(System.currentTimeMillis() / 1000);
//                        ByteBuf byteBuf = PacketFactory.buildPacket(channel, head, heartBeatMsg);
//                        channel.writeAndFlush(byteBuf, channel.voidPromise());
//                        Thread.sleep(10);
//                    } catch (Exception e) {
//                        Log.error("heartBeat error", e);
//                    }
//                }
//            }
//        }, 5000, 3000);
//
//    }

    /**
     * <pre>
     * 重连某个连接
     * </pre>
     */
    private Channel reconnect(int randIdx) {
        if (randIdx < 0 || randIdx > chs.size() - 1) {
            return null;
        }
        Channel ch = null;
        synchronized (chs) {
            ch = chs.get(randIdx);
            try {
                if (ch != null) {
                    ch.close();
                }
            } catch (Throwable e) {
                Log.error("close channel error,ch:" + ch, e);
            }
            try {
                ch = b.connect(this.ip, this.port).sync().channel();
                chs.set(randIdx, ch);
            } catch (Throwable e) {
                Log.error("reconnect " + serverName + " error,ip：" + ip + ",port:" + port, e);
            }
        }
        if (isActive(ch)) {
            Log.info("reconnect " + serverName + " succ,ip：" + ip + ",port:" + port);
        }
        return ch;
    }

    /**
     * <pre>
     * 获取连接集合
     * </pre>
     *
     */
//    public final List<Channel> getChs() {
//        return chs;
//    }

    /**
     * <pre>
     * 随机获取连接
     * </pre>
     */
    public final Channel selectChannel() {
        final List<Channel> chs = this.chs;
        if (chs.isEmpty()) {
            return null;
        }
        int randIdx = RandomUtil.rand(chs.size() - 1);
        Channel conn = chs.get(randIdx);
        // 检测连接
        if (!isActive(conn)) {
            // 重连服务器
            conn = reconnect(randIdx);
            if (!isActive(conn)) {
                return null;
            }
        }
        return conn;
    }

    /**
     * <pre>
     * 连接是否有效
     * </pre>
     */
    private static boolean isActive(Channel channel) {
        return channel != null && channel.isOpen() && channel.isActive();
    }

    /**
     * <pre>
     * 设置参数
     * </pre>
     */
    protected abstract void initParam(Bootstrap b);

    /**
     * <pre>
     * 停止客户端
     * </pre>
     */
    public final void stop() {
        try {
            synchronized (chs) {
                for (int i = 0, len = chs.size(); i < len; i++) {
                    chs.get(i).close();
                    chs.get(i).closeFuture().syncUninterruptibly();
                }
            }
            workerGroup.shutdownGracefully();
        } catch (Exception e) {
            Log.error(serverName + "'s client close error", e);
        }
    }

    /**
     * <pre>
     * 创建客户端
     * </pre>
     */
    public final static BaseNettyClient createInstance(Class<?> clazz, String name, String host, int port, int workThreadCount, ChannelInitializer<SocketChannel> serverInitializer) {
        try {
            Constructor<?> constructor = clazz.getConstructor(String.class, String.class, int.class, int.class, ChannelInitializer.class);
            if (constructor == null) {
                Log.error("构造方法参数错误：" + clazz.getName());
                return null;
            }
            constructor.setAccessible(true);
            return (BaseNettyClient) constructor.newInstance(name, host, port, workThreadCount, serverInitializer);
        } catch (Throwable e) {
            Log.error("", e);
        }
        return null;
    }

}
