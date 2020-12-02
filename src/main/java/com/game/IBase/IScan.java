package com.game.IBase;

/**
 * <pre>
 * 扫描
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 09:43
 */
public interface IScan {
    /**
     * <pre>
     * 每秒调用一次
     * </pre>
     */
    void scanSecond();

    /**
     * <pre>
     * 每分钟调用一次
     * </pre>
     */
    void scanMinute();
}
