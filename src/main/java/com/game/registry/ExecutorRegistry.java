package com.game.registry;

import com.game.executor.base.IExecutor;
import com.game.executor.task.BaseDelayTask;
import com.game.executor.task.BaseTask;

/**
 * <pre>
 * 带执行器的注册表
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-26 16:49
 */
public class ExecutorRegistry<K, V> extends BaseRegistry<K, V> {

    protected IExecutor executor;

    public ExecutorRegistry() {
    }

    public void init(String name, int threadCount, int capacity, int type) throws Exception {
        this.executor = IExecutor.build(name, threadCount, capacity, type);
    }

    public void submitTask(K key, BaseTask task) {
        final V object = get(key);
        if (object == null) {
            return;
        }
        executor.submit(object.hashCode(), task);
    }

    public void submitTaskS(Object obj, BaseTask task) {
        if (obj == null) {
            return;
        }
        executor.submit(obj.hashCode(), task);
    }

    public void submitDelay(K key, BaseDelayTask task) {
        final V object = get(key);
        if (object == null) {
            return;
        }
        executor.submitDelay(object.hashCode(), task);
    }

    public void submitDelay0(BaseDelayTask task) {
        executor.submit0Delay(task);
    }

    public void submitDelayS(Object obj, BaseDelayTask task) {
        if (obj == null) {
            return;
        }
        executor.submitDelay(obj.hashCode(), task);
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

}
