package com.game.executor.task;

import com.game.executor.base.EQueue;
import com.game.utils.Log;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * 任务基类
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-10 18:02
 */
public abstract class BaseTask implements Runnable {

    /**
     * 创建时间
     */
    private long createMills;

    /**
     * 开始执行时间
     */
    private long startMills;

    /**
     * 当前状态 创建0 运行1 结束2
     */
    protected AtomicInteger status;

    protected AtomicInteger forceStop;

    private EQueue<BaseTask> queue;
    private Future<?> future;


    public BaseTask() {
        this.createMills = System.currentTimeMillis();
        this.forceStop = new AtomicInteger();
        this.status = new AtomicInteger();
        this.startMills = Long.MAX_VALUE;
    }

    public void setQueue(EQueue<BaseTask> queue) {
        this.queue = queue;
    }

    public EQueue<BaseTask> getQueue() {
        return queue;
    }

    public void setOver() {
        this.status.set(2);
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    protected boolean isTimeout() {
        return System.currentTimeMillis() - startMills > 3000;
    }

    public boolean checkTimeout() {
        if (isTimeout() && forceStop.compareAndSet(0, 1)) {
            Log.error("TaskForceStop task:" + toString());
            this.future.cancel(true);
            return true;
        }
        return false;
    }


    @Override
    public void run() {
        if (status.compareAndSet(0, 1)) {
            try {
                startMills = System.currentTimeMillis();
                exec();
                long ct = System.currentTimeMillis() - startMills;
                long qtime = System.currentTimeMillis() - createMills;
                if (ct > 100) {
                    Log.warn("TaskCostLongTime:" + ct + "ms,qtime:" + qtime + "ms," + toString());
                }
            } catch (Exception e) {
                Log.error("TaskExecError:" + toString(), e);
            } finally {
                afterExec();
            }
        }
    }

    protected void afterExec() {
        if (status.compareAndSet(1, 2)) {
            queue.next(this);
        }
    }

    protected abstract void exec() throws Exception;

    protected abstract String getName();

    @Override
    public String toString() {
        return "Task-" + getName();
    }

}
