package com.net.main.server;

import com.game.utils.Log;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * Server基类
 * </pre>
 *
 * @author yuxuan
 * @time 2020-09-17 10:01
 */
public abstract class BaseServer {

    /**
     * 服务器单例
     */
    protected static BaseServer server;

    /**
     * 服务器状态
     */
    public final static AtomicInteger status = new AtomicInteger(0);

    /**
     * <pre>
     * 启动
     * </pre>
     */
    public abstract void start(String[] args) throws Exception;

    /**
     * <pre>
     * 停服前执行
     * </pre>
     */
    protected abstract void beforeShutdown();

    /**
     * <pre>
     * 停服
     * </pre>
     */
    public abstract void shutdown();

    /**
     * <pre>
     * 停服钩子
     * </pre>
     */
    protected void addShutdownHook() {
        final BaseServer server = this;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Log.info(server.getClass().getSimpleName() + " ExecShutdownHook1...");
                server.beforeShutdown();
                Log.info(server.getClass().getSimpleName() + " ExecShutdownHook2...");
                LogManager.shutdown();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, "Thread-ShutdownHook-" + this.getClass().getSimpleName()));
    }

}
