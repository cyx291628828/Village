package com.game.executor.transfer;


import com.game.executor.base.BaseExecutor;
import com.game.executor.base.EQueue;
import com.game.executor.base.IPull;
import com.game.executor.driven.DelayDrivenEQueue;

/**
 * <pre>
 * 核心执行器-传输模型
 * ！尚未完成
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-10 15:29
 */
public class TransferExecutor extends BaseExecutor {

    // 传输线程
    private IPull puller;


    public TransferExecutor(final String name, final int threadCount, final int capacity) {
        super(name, threadCount, capacity);

        // 初始化拉取线程
        initRunnable();
        // 启动拉取线程
        new Thread(puller, "Thread-Transfer-" + name).start();

        // 状态监控任务
//        submit0Delay(new BaseDelayTask(10 * 1000, 10 * 1000) {
//            @Override
//            protected void exec() throws Exception {
//                monitor();
//            }
//
//            @Override
//            protected String getName() {
//                return "ExecutorStatus";
//            }
//        });
    }

    @Override
    protected void initQueue(final int threadCount, final int capacity) {
        this.qSize = threadCount + 1;
        this.queues = new TransferEQueue[this.qSize];
        for (int i = 0, len = queues.length; i < len; i++) {
            queues[i] = new TransferEQueue<>(name, capacity);
        }
        this.delayQueue = new DelayDrivenEQueue(name);
    }

    private void initRunnable() {
        puller = new TransferPuller(this);
        puller.setQueues(queues);
        for (int i = 0, len = queues.length; i < len; i++) {
            queues[i].setPuller(puller);
        }
        new Thread(puller, "Thread-Pull-" + name).start();
    }

    @Override
    public void shutdown() {
        this.puller.shutdown();
        this.delayQueue.shutdown();
        this.threadPool.shutdown();
    }

    private void monitor() {
        for (EQueue queue : queues) {
            queue.printStatus();
        }
    }

    @Override
    public String toString() {
        return "TransferExecutor:" + name + ",qSize:" + qSize;
    }

}
