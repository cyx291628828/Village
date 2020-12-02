package com.game.executor.wait;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 * await等待
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-27 10:48
 */
public class IWaitForAwait implements IWaitFor {

    private ReentrantLock lock;
    private Condition condition;

    public IWaitForAwait() {
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    @Override
    public long waitFor(TimeUnit timeUnit, long time, long counter) throws InterruptedException {
        if (time <= 0) {
            return counter;
        }
        try {
            lock.lock();
            condition.awaitNanos(timeUnit.toNanos(time));
        } finally {
            lock.unlock();
        }
        return counter + 1;
    }

    @Override
    public void signalAll() {
        try {
            lock.lock();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
