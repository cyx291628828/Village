package com.game.executor.wait;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 阶段等待
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-27 10:48
 */
public class IWaitForStage implements IWaitFor {

    private IWaitFor yield;
    private IWaitFor await;
    private IWaitFor park;
    private long awaitMills;

    public IWaitForStage(long awaitMills) {
        this.yield = new IWaitForYield();
        this.await = new IWaitForAwait();
        this.park = new IWaitForPark();
        this.awaitMills = awaitMills;
    }

    @Override
    public long waitFor(TimeUnit timeUnit, long time, long counter) throws InterruptedException {
        if (time <= 0) {
            return counter;
        }
        if (counter < 20) {
            counter = yield.waitFor(timeUnit, time, counter);
        } else if (counter < 200) {
            counter = park.waitFor(timeUnit, time, counter);
        } else {
            counter = await.waitFor(TimeUnit.MILLISECONDS, awaitMills, counter);
        }

        return counter;
    }

    @Override
    public void signalAll() {
        await.signalAll();
    }
}
