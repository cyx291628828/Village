package com.game.executor.base;

import com.game.executor.driven.SelfDrivenExecutor;
import com.game.executor.pull.PulledExecutor;
import com.game.executor.task.BaseDelayTask;
import com.game.executor.task.BaseTask;
import com.game.executor.transfer.TransferExecutor;

/**
 * <pre>
 * 执行器接口
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-23 16:52
 */
public interface IExecutor {

    void submit0(BaseTask task);

    void submit(int hashCode, BaseTask task);

    void submitDelay(int hashCode, BaseDelayTask delayTask);

    void submit0Delay(BaseDelayTask delayTask);

    void run(BaseTask task);

    void shutdown();

    String getName();


    /**
     * <pre>
     * 执行器构建统一入口
     * </pre>
     */
    static IExecutor build(String name, int threadCount, int capacity, int executorType) {
        if (executorType == 1) {
            // 自驱动、当前正在执行任务超时检测
            return new SelfDrivenExecutor(name, threadCount, capacity);
        } else if (executorType == 2) {
            // 拉取式、当前正在执行任务超时检测、队列负载较高时，移除旧包
            return new PulledExecutor(name, threadCount, capacity);
        } else if (executorType == 3) {
            return new TransferExecutor(name, threadCount, capacity);
        }
        return null;
    }

}
