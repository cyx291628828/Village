package com.game.executor.wait;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * <pre>
 * park等待
 * </pre>
 *
 * @author yuxaun
 * @time 2020-10-27 10:48
 */
public class IWaitForPark implements IWaitFor {

    @Override
    public long waitFor(TimeUnit timeUnit, long time, long counter) throws InterruptedException {
        if (time <= 0) {
            return counter;
        }
        LockSupport.parkNanos(timeUnit.toNanos(time));
        return counter + 1;
    }

    @Override
    public void signalAll() {
    }
}
