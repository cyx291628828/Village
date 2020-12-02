package com.game.executor.pull;

import com.game.executor.base.EQueue;
import com.game.executor.base.IExecutor;
import com.game.executor.base.IPull;
import com.game.executor.task.BaseTask;
import com.game.executor.wait.IWaitFor;
import com.game.executor.wait.IWaitForStage;
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
public class PullRunnable implements IPull {

    private volatile boolean running;
    private IExecutor executor;
    private IWaitFor waiter;
    private EQueue<BaseTask>[] queues;

    PullRunnable(final IExecutor executor) {
        this.waiter = new IWaitForStage(1000L);
//        this.waiter = new IWaitForAwait();
        this.executor = executor;
        this.running = true;
    }

    @Override
    public void run() {
        int emptyLoop = 0;
        while (running) {
            if (emptyLoop > 0) {
                try {
                    waiter.waitFor(TimeUnit.NANOSECONDS, 100_000L, emptyLoop);
//                    waiter.waitFor(TimeUnit.MICROSECONDS, 100_000L, emptyLoop);
                } catch (Exception e) {
                    Log.error("", e);
                }
                if (emptyLoop >= 200) {
                    emptyLoop = 0;
                }
            }
            boolean haveTask = false;
            for (EQueue<BaseTask> queue : queues) {
                try {
                    BaseTask task = queue.poll();
                    if (task == null) {
                        continue;
                    }
                    haveTask = true;
                    executor.run(task);
                } catch (Exception e) {
                    Log.error("", e);
                }
            }
            if (!haveTask) {
                emptyLoop++;
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
