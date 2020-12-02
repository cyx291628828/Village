package com.game.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <pre>
 * 日志工具类
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public class Log {

    public final static String NEW_LINE = System.getProperty("line.separator");
    private final static String THIS_CLASS = Log.class.getName();
    private final static Logger logger;

    static {
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        logger = LogManager.getLogger("-");
    }

    private static String getMethodLine(StackTraceElement last) {
        String fileName = last.getFileName();
        final int lineNumber = last.getLineNumber();
        if (fileName == null) {
            fileName = "Unknown Source";
        }
        return fileName + ":" + lineNumber;
    }

    /**
     * <pre>
     * 获取堆栈信息
     * </pre>
     *
     * @param msg 日志
     * @return String
     */
    private static String getStackMsg(String msg) {
        StackTraceElement last = null;
        final String thisClazz = THIS_CLASS;
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int i = stackTrace.length - 1; i > 0; i--) {
            final String className = stackTrace[i].getClassName();
            if (thisClazz.equals(className)) {
                last = stackTrace[i + 1];
                return last == null ? "no stack" : getMethodLine(last) + NEW_LINE + msg;
            }
        }
        return "";
    }

    public static void debug(String msg) {
        String message = getStackMsg(msg);
        logger.debug(message);
    }


    public static void info(String msg) {
        String message = getStackMsg(msg);
        logger.info(message);
    }


    public static void warn(String msg) {
        String message = getStackMsg(msg);
        logger.warn(message);
    }


    public static void error(String msg) {
        String message = getStackMsg(msg);
        logger.error(message);
    }

    public static void error(String msg, Throwable t) {
        String message = getStackMsg(msg);
        if (logger != null) {
            logger.error(message, t);
        }
    }

    public static void fatal(String msg) {
        String message = getStackMsg(msg);
        logger.fatal(message);
    }

    public static void fatal(String msg, Throwable t) {
        String message = getStackMsg(msg);
        logger.fatal(message, t);
    }


}
