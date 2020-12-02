package com.game.executor.base;


import com.game.executor.driven.DelayDrivenEQueue;
import com.game.executor.task.BaseDelayTask;
import com.game.executor.task.BaseTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * 核心执行器-基类
 * </pre>
 *
 * @author yuxaun
 * @time 2020-11-10 15:29
 */
public abstract class BaseExecutor implements IExecutor {

    protected ThreadPoolExecutor threadPool;
    protected DelayDrivenEQueue delayQueue;
    protected EQueue[] queues;
    protected String name;
    protected int qSize;


    public BaseExecutor(final String name, final int threadCount, final int capacity) {
        this(name, threadCount, threadCount, capacity);
    }

    public BaseExecutor(final String name, final int threadCount, final int queueCount, final int capacity) {
        this.name = name;
        // 初始化执行队列
        initQueue(queueCount, capacity);
        // 线程序号
        final AtomicInteger index = new AtomicInteger();
        // 初始化池队列(一般不会满)
        int tempSize = queueCount / threadCount + 16;
        final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(tempSize);
        // 舍弃最老的任务
        final RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        // 线程工厂
        final ThreadFactory threadFactory = (r) -> new Thread(r, "Thread-" + name + "-" + index.incrementAndGet());
        // 线程池
        this.threadPool = new ThreadPoolExecutor(threadCount, threadCount, 0, TimeUnit.MILLISECONDS, queue, threadFactory, handler);
        this.threadPool.prestartAllCoreThreads();
    }

    /**
     * <pre>
     * 初始化执行队列
     * </pre>
     */
    protected abstract void initQueue(final int threadCount, final int capacity);

    /**
     * <pre>
     * 放默认队列执行
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void submit0(BaseTask task) {
        task.setQueue(queues[0]);
        queues[0].submit(task);
    }

    @SuppressWarnings("unchecked")
    private EQueue setQueue(int hashCode, BaseTask task) {
        int queueIdx = hashCode % (qSize - 1) + 1;
        EQueue queue = queues[queueIdx];
        task.setQueue(queue);
        return queue;
    }

    /**
     * <pre>
     * 放绑定队列执行
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void submit(int hashCode, BaseTask task) {
        setQueue(hashCode, task).submit(task);
    }

    /**
     * <pre>
     * 放绑定队列延时执行
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void submitDelay(int hashCode, BaseDelayTask delayTask) {
        setQueue(hashCode, delayTask);
        delayTask.setDelayQueue(delayQueue);
        delayQueue.submit(delayTask);
    }

    /**
     * <pre>
     * 放默认队列延时执行
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void submit0Delay(BaseDelayTask delayTask) {
        delayTask.setDelayQueue(delayQueue);
        delayTask.setQueue(queues[0]);
        delayQueue.submit(delayTask);
    }

    /**
     * <pre>
     * 提交运行-仅供队列调用
     * </pre>
     */
    @Override
    public void run(BaseTask task) {
        if (task == null) {
            return;
        }
        Future<?> future = this.threadPool.submit(task);
        task.setFuture(future);
    }

    @Override
    public void shutdown() {
        this.delayQueue.shutdown();
        this.threadPool.shutdown();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BaseExecutor:" + name + ",qSize:" + qSize;
    }
}
