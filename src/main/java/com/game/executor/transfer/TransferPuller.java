package com.game.executor.transfer;


import com.game.executor.base.EQueue;
import com.game.executor.base.IExecutor;
import com.game.executor.base.IPull;
import com.game.executor.task.BaseTask;
import com.game.executor.wait.IWaitFor;
import com.game.executor.wait.IWaitForAwait;
import com.game.utils.Log;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 拉取执行
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-23 16:34
 */
public class TransferPuller implements IPull {

    private volatile boolean running;
    private IExecutor executor;
    private IWaitFor waiter;
    private EQueue<BaseTask>[] queues;

    TransferPuller(final IExecutor executor) {
        this.waiter = new IWaitForAwait();
        this.executor = executor;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            for (EQueue<BaseTask> queue : queues) {
                try {
                    BaseTask task = queue.poll(1000, TimeUnit.NANOSECONDS);
                    if (task == null) {
                        continue;
                    }
                    for (int i = 1; task != null && i <= 10; i++) {
                        executor.run(task);
                        task = queue.poll();
                    }
                } catch (Exception e) {
                    Log.error("", e);
                }
            }
        }
    }

    @Override
    public void setQueues(EQueue[] queues) {
        this.queues = queues;
    }

    @Override
    public void resume() {
        this.waiter.signalAll();
    }

    @Override
    public void shutdown() {
        this.running = false;
    }

}
