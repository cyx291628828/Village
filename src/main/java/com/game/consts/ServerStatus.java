package com.game.consts;

/**
 * <pre>
 * 服务器状态
 * </pre>
 *
 * @author yuxuan
 * @time 2020-09-17 10:15
 */
public interface ServerStatus {

    /**
     * 正在启动
     */
    short STARTING = 1;

    /**
     * 正常运行
     */
    short RUNNING = 2;

    /**
     * 正在重置
     */
    short RESETTING = 3;

    /**
     * 正在停服
     */
    short STOPPING = 4;

}
