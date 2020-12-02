package com.game.executor.task;

import com.game.executor.driven.DelayDrivenEQueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 延时任务基类
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-18 11:11
 */
public abstract class BaseDelayTask extends BaseTask implements Delayed {

    private final static long SECOND_1_NANOS = TimeUnit.SECONDS.toNanos(1);

    private DelayDrivenEQueue<BaseDelayTask> delayQueue;

    /**
     * <pre>
     * 第一次执行时间戳
     * </pre>
     */
    private volatile long execMills;

    /**
     * <pre>
     * 周期执行间隔ms
     * </pre>
     */
    private volatile long interval;

    public BaseDelayTask(long delayMills) {
        if (delayMills < 0) {
            throw new IllegalArgumentException("delayMills must >= 0");
        }
        this.execMills = System.currentTimeMillis() + delayMills;
        this.interval = 0;
    }

    public BaseDelayTask(long delayMills, long interval) {
        if (delayMills < 0) {
            throw new IllegalArgumentException("delayMills must >= 0");
        }
        if (interval < 100) {
            throw new IllegalArgumentException("interval must >= 100");
        }
        this.execMills = System.currentTimeMillis() + delayMills;
        this.interval = interval;
    }

    public void setDelayQueue(DelayDrivenEQueue<BaseDelayTask> delayQueue) {
        this.delayQueue = delayQueue;
    }

    /**
     * <pre>
     * 停止周期运行
     * </pre>
     */
    public void stopInterval() {
        this.interval = 0;
    }

    /**
     * <pre>
     * 更改执行时间
     * </pre>
     *
     * @param mills 正数延后、负数提前，单位ms
     */
    public void opExecMills(long mills) {
        execMills = execMills + mills;
    }

    /**
     * <pre>
     * 距离下次执行剩余ms
     * </pre>
     */
    public long getLeftMills() {
        return execMills - System.currentTimeMillis();
    }

    public long getExecMills() {
        return execMills;
    }

    @Override
    protected boolean isTimeout() {
        return false;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        final long sec1Nanos = SECOND_1_NANOS;
        long nanos = unit.convert(execMills - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        return Math.min(nanos, sec1Nanos);
    }


    @Override
    public int compareTo(Delayed d) {
        BaseDelayTask next = (BaseDelayTask) d;
        return (int) (this.execMills - next.execMills);
    }

    @Override
    protected void afterExec() {
        super.afterExec();
        if (interval > 0 && status.compareAndSet(2, 0)) {
            execMills = System.currentTimeMillis() + interval;
            delayQueue.submit(this);
        }
    }

    @Override
    public String toString() {
        return "DelayTask-" + getName();
    }

}
