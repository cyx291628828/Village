package com.game.executor.pull;


import com.game.executor.base.BaseExecutor;
import com.game.executor.base.EQueue;
import com.game.executor.base.IPull;
import com.game.executor.driven.DelayDrivenEQueue;
import com.game.executor.task.BaseDelayTask;
import com.game.utils.RandomUtil;

/**
 * <pre>
 * 核心执行器-Pull模型
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-10 15:29
 */
public class PulledExecutor extends BaseExecutor {

    // 拉取线程
    private IPull puller;

    public PulledExecutor(final String name, final int threadCount, final int capacity) {
        this(name, threadCount, threadCount, capacity);
    }

    public PulledExecutor(final String name, final int threadCount, final int queueCount, final int capacity) {
        super(name, threadCount, queueCount, capacity);

        // 初始化拉取线程
        initRunnable();
        // 状态监控任务
        final long initMills = RandomUtil.rand(5000, 10000);
        submit0Delay(new BaseDelayTask(initMills, 10 * 1000) {
            @Override
            protected void exec() throws Exception {
                monitor();
            }

            @Override
            protected String getName() {
                return "ExecutorStatus-" + name;
            }
        });
    }

    @Override
    protected void initQueue(int threadCount, int capacity) {
        this.qSize = threadCount + 1;
        this.queues = new PulledEQueue[this.qSize];
        for (int i = 0, len = queues.length; i < len; i++) {
            queues[i] = new PulledEQueue<>(name, capacity);
        }
        this.delayQueue = new DelayDrivenEQueue(name);
    }

    private void initRunnable() {
        puller = new PullRunnable(this);
        puller.setQueues(queues);
        for (int i = 0, len = queues.length; i < len; i++) {
            queues[i].setPuller(puller);
        }
        new Thread(puller, "Thread-Pull-" + name).start();
    }


    private void monitor() {
        for (EQueue queue : queues) {
            queue.printStatus();
        }
    }

    @Override
    public void shutdown() {
        this.puller.shutdown();
        this.delayQueue.shutdown();
        this.threadPool.shutdown();
    }


    @Override
    public String toString() {
        return "PulledExecutor:" + name + ",qSize:" + qSize;
    }

}
