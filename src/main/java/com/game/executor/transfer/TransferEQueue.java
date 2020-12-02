package com.game.executor.transfer;

import com.game.executor.base.EQueue;
import com.game.executor.base.IPull;
import com.game.executor.task.BaseTask;
import com.game.utils.JsonUtil;
import com.game.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 * 执行队列-Transfer模型
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-10 17:44
 */
public class TransferEQueue<E extends BaseTask> implements EQueue<E> {

    private LinkedTransferQueue<E> taskQueue;
    private List<String> discardTasks;
    private AtomicBoolean isBusy;
    private String name;
    private int halfCap;


    TransferEQueue(final String name, final int capacity) {
        this.name = name;
        this.halfCap = capacity;
        this.discardTasks = new ArrayList<>();
        this.isBusy = new AtomicBoolean(false);
        this.taskQueue = new LinkedTransferQueue<>();
    }

    @Override
    public void submit(E task) {
        if (!taskQueue.offer(task)) {
            Log.error("QueueFull name:" + name + ",halfCap:" + halfCap + ",isBusy:" + isBusy.get());
        }
    }

    @Override
    public E poll() {
        if (!isBusy.compareAndSet(false, true)) {
            return null;
        }
        E e = taskQueue.poll();
        if (e == null) {
            isBusy.compareAndSet(true, false);
        }
        return e;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        if (!isBusy.compareAndSet(false, true)) {
            return null;
        }
        E e = taskQueue.poll(timeout, unit);
        if (e == null) {
            isBusy.compareAndSet(true, false);
        }
        return e;
    }

    @Override
    public void next(E task) {
        isBusy.compareAndSet(true, false);
    }

    public void printStatus() {
        if (discardTasks.isEmpty()) {
            return;
        }
        List<String> tempTasks = new ArrayList<>(discardTasks);
        discardTasks.clear();

        Log.warn("PulledQueue:" + name + ",load:" + taskQueue.size() + "/" + (halfCap * 2) + ",discard:" + tempTasks.size() + " " + JsonUtil.stringify(tempTasks));
    }

    @Override
    public void setPuller(IPull puller) {
    }

    @Override
    public String toString() {
        return "TransferEQueue:" + name + ",halfCap:" + halfCap + ",isBusy:" + isBusy.get();
    }
}
