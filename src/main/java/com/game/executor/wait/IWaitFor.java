package com.game.executor.wait;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 等待策略
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-27 10:37
 */
public interface IWaitFor {

    long waitFor(TimeUnit timeUnit, long time, long counter) throws InterruptedException;

    void signalAll();
}
