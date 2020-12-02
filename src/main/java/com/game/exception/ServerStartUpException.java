package com.game.exception;

/**
 * <pre>
 * 启动异常
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-25 18:03
 */
public class ServerStartUpException extends RuntimeException {

    public ServerStartUpException(String message) {
        super(message);
    }
}
