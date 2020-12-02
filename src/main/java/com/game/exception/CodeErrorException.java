package com.game.exception;

/**
 * <pre>
 * 协议号错误
 * </pre>
 *
 * @author yuxuan
 */
public class CodeErrorException extends RuntimeException {

    public CodeErrorException(String message) {
        super(message);
    }
}
