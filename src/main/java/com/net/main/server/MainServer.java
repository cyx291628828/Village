package com.net.main.server;

import com.game.consts.ServerStatus;
import com.game.utils.Log;
import com.net.handler.VillageServerHandler;
import com.net.main.initializer.SocketServerInitializer;
import org.apache.logging.log4j.LogManager;

/**
 * <pre>
 * 大厅服务器(临时版本)
 * </pre>
 *
 * @author yuxuan
 * @time 2020-03-11 16:27
 */
public class MainServer extends BaseServer {

    private BaseNettyServer socketServer;


    /**
     * <pre>
     * 停服逻辑
     * </pre>
     */
    @Override
    protected void beforeShutdown() {
        status.set(ServerStatus.STOPPING);
        // 关闭socket
        if (this.socketServer != null) {
            this.socketServer.close();
        }
    }

    @Override
    public void shutdown() {
        beforeShutdown();
        LogManager.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        try {
            server = new MainServer();
            server.start(args);
        } catch (Exception e) {
            Log.error("StartUpError:", e);
            System.exit(0);
        }
    }


    @Override
    public void start(String[] args) throws Exception {
        if (!status.compareAndSet(0, ServerStatus.STARTING)) {
            System.exit(0);
            return;
        }
        addShutdownHook();

        this.socketServer = BaseNettyServer.createInstance(BaseSocketServer.class, 9624, 8, new SocketServerInitializer(new VillageServerHandler()));

        if (!status.compareAndSet(ServerStatus.STARTING, ServerStatus.RUNNING)) {
            shutdown();
        }

    }


}
