package com.game.executor.pull;


import com.game.executor.base.EQueue;
import com.game.executor.base.IPull;
import com.game.executor.task.BaseDelayTask;
import com.game.executor.task.BaseTask;
import com.game.utils.Log;
import org.jctools.queues.MpmcArrayQueue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <pre>
 * 执行队列-Pull模型
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-23 17:44
 */
public class PulledEQueue<E extends BaseTask> implements EQueue<E> {

    private AtomicReference<E> lastTask;
    private MpmcArrayQueue<E> queue;
    private AtomicBoolean isBusy;
    private IPull puller;
    private String name;
    private int halfCap;


    public PulledEQueue(final String name, final int capacity) {
        this.name = name;
        this.halfCap = capacity;
        this.isBusy = new AtomicBoolean(false);
        this.lastTask = new AtomicReference<>(null);
        this.queue = new MpmcArrayQueue<>(capacity * 2);
    }

    @Override
    public void submit(E task) {
        if (!queue.offer(task)) {
            Log.error("QueueFull name:" + name + ",halfCap:" + halfCap + ",isBusy:" + isBusy.get());
            return;
        }
        E last = lastTask.getAndSet(null);
        if (last != null) last.checkTimeout();

        puller.resume();
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("DelayDrivenEQueue not support this method");
    }

    @Override
    public E poll() {
        if (!isBusy.compareAndSet(false, true)) {
            E last = lastTask.getAndSet(null);
            if (last != null) last.checkTimeout();

            return null;
        }
        for (int i = 0, size = queue.size(); i < 10 && size > halfCap; i++) {
            E rmTask = queue.relaxedPoll();
            if (rmTask != null) {
                if (rmTask instanceof BaseDelayTask) {
                    queue.offer(rmTask);
                } else {
                    size--;
                }
            }
        }
        E e = queue.poll();
        if (e == null) {
            isBusy.compareAndSet(true, false);
        } else {
            lastTask.set(e);
        }
        return e;
    }

    @Override
    public void next(E task) {
        if (isBusy.compareAndSet(true, false)) {
            lastTask.compareAndSet(task, null);
            puller.resume();
        }
    }

    public void printStatus() {
        final int size = queue.size();
        if (size > 1) {
            Log.info("QMonitor-" + name + ":" + size + "/" + halfCap);
        }
    }

    @Override
    public void setPuller(IPull puller) {
        this.puller = puller;
    }

    @Override
    public String toString() {
        return "PulledQueue:" + name + ",halfCap:" + halfCap + ",isBusy:" + isBusy.get();
    }
}
