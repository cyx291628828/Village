package com.game.executor.driven;


import com.game.executor.base.EQueue;
import com.game.executor.base.IExecutor;
import com.game.executor.base.IPull;
import com.game.executor.task.BaseTask;
import com.game.utils.Log;
import org.jctools.queues.SpscArrayQueue;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 * 执行队列-自驱动模型
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-10 17:44
 */
public class SelfDrivenEQueue<E extends BaseTask> implements EQueue<E> {

    private Queue<BaseTask> taskQueue;
    private AtomicBoolean isBusy;
    private AtomicBoolean lock;
    private IExecutor executor;
    private int capacity;


    public SelfDrivenEQueue(IExecutor executor, int capacity) {
        this.isBusy = new AtomicBoolean(false);
        this.lock = new AtomicBoolean(false);
        this.taskQueue = new SpscArrayQueue<>(capacity);
        this.capacity = capacity;
        this.executor = executor;
    }

    private void checkTimeout() {
        BaseTask oldest = this.taskQueue.peek();
        if (oldest != null) oldest.checkTimeout();
    }

    @Override
    public void submit(E task) {
        if (task == null) {
            return;
        }
        while (true) {
            if (lock.compareAndSet(false, true)) {
                // 入队列
                if (!this.taskQueue.offer(task)) {
                    checkTimeout();
                    Log.error("QueueFull,task:" + task.toString() + "," + this.executor);
                } else {
                    // 当前队列空闲
                    if (this.isBusy.compareAndSet(false, true)) {
                        this.executor.run(this.taskQueue.peek());
                    }
                    // 强制退出超时任务
                    else {
                        checkTimeout();
                    }
                }
                lock.compareAndSet(true, false);
                break;
            }
        }
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
        while (true) {
            if (lock.compareAndSet(false, true)) {
                BaseTask top = this.taskQueue.poll();
                if (top == null) {
                    this.isBusy.compareAndSet(true, false);
                    Log.error("QueueEmpty,last:" + last.toString());
                    lock.compareAndSet(true, false);
                    break;
                }
                if (top != last) {
                    this.executor.run(top);
                    Log.error("TaskDifferent,last:" + last.toString() + ",top:" + top.toString());
                    lock.compareAndSet(true, false);
                    break;
                }

                BaseTask next = this.taskQueue.peek();
                if (next != null) {
                    this.executor.run(next);
                } else {
                    this.isBusy.compareAndSet(true, false);
                }
                lock.compareAndSet(true, false);
                break;
            }
        }
    }

    @Override
    public void printStatus() {

    }

    @Override
    public void setPuller(IPull puller) {
        throw new UnsupportedOperationException("DelayDrivenEQueue not support this method");
    }

    @Override
    public String toString() {
        return executor.getName() + ":" + this.capacity;
    }
}
