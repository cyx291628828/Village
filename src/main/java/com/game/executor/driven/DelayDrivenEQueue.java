package com.game.executor.driven;


import com.game.executor.base.EQueue;
import com.game.executor.base.IPull;
import com.game.executor.task.BaseDelayTask;
import com.game.utils.Log;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 延时队列-自驱动模型
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-18 10:26
 */
public class DelayDrivenEQueue<E extends BaseDelayTask> implements EQueue<E> {


    private LoopPollRunnable pollRunnable;

    private DelayQueue<E> delayQueue;


    public DelayDrivenEQueue(final String name) {
        this.delayQueue = new DelayQueue<>();
        this.pollRunnable = new LoopPollRunnable<>(this.delayQueue);
        new Thread(pollRunnable, "Thread-DelayQueue-" + name).start();
    }

    static class LoopPollRunnable<E extends BaseDelayTask> implements Runnable {

        private DelayQueue<E> delayQueue;
        private volatile boolean isRun;

        public LoopPollRunnable(DelayQueue<E> delayQueue) {
            this.delayQueue = delayQueue;
            this.isRun = true;
        }

        public void setRun(boolean run) {
            this.isRun = run;
        }

        @Override
        public void run() {
            while (isRun) {
                try {
                    E t = delayQueue.take();
                    t.getQueue().submit(t);
                } catch (Throwable e) {
                    Log.error("", e);
                }
            }
        }
    }


    @Override
    public void submit(E task) {
        if (task == null) {
            return;
        }
        delayQueue.offer(task);
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("DelayDrivenEQueue not support this method");
    }

    @Override
    public E poll() {
        throw new UnsupportedOperationException("DelayDrivenEQueue not support this method");
    }

    @Override
    public void next(E last) {
        throw new UnsupportedOperationException("DelayDrivenEQueue not support this method");
    }

    @Override
    public void printStatus() {
    }

    @Override
    public void setPuller(IPull puller) {
        throw new UnsupportedOperationException("DelayDrivenEQueue not support this method");
    }

    public void shutdown() {
        this.pollRunnable.setRun(false);
    }

}
