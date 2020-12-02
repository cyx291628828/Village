package com.game.executor.driven;


import com.game.executor.base.BaseExecutor;

/**
 * <pre>
 * 核心执行器-自驱动模型
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-10 15:29
 */
public class SelfDrivenExecutor extends BaseExecutor {


    public SelfDrivenExecutor(final String name, final int threadCount, final int capacity) {
        super(name, threadCount, capacity);
    }

    @Override
    protected void initQueue(final int threadCount, final int capacity) {
        this.qSize = threadCount + 1;
        this.queues = new SelfDrivenEQueue[this.qSize];
        for (int i = 0, len = queues.length; i < len; i++) {
            queues[i] = new SelfDrivenEQueue(this, capacity);
        }
        this.delayQueue = new DelayDrivenEQueue(name);
    }

    @Override
    public String toString() {
        return "SelfDrivenExecutor:" + name + ",qSize:" + qSize;
    }
}
