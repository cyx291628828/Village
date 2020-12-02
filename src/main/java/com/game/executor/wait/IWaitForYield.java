package com.game.executor.wait;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * yield等待
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-27 10:48
 */
public class IWaitForYield implements IWaitFor {

    @Override
    public long waitFor(TimeUnit timeUnit, long time, long counter) throws InterruptedException {
        if (time <= 0) {
            return counter;
        }
        Thread.yield();
        return counter + 1;
    }

    @Override
    public void signalAll() {
    }
}
