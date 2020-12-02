package com.net.common;

import com.game.executor.base.IExecutor;
import com.game.executor.task.BaseDelayTask;
import com.game.utils.RandomUtil;
import com.net.handler.IElm;
import com.net.handler.IHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 通用执行器
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-31 19:38
 */
public class CommonExecutor<E extends IElm> {

    private String name;
    private IHandler<E> handler;
    private CommonPuller<E> puller;
    private static IExecutor monitor;
    private Map<Object, CommonPullQueue<E>> queues;

    public CommonExecutor(String name, IHandler<E> handler) {
        this.name = name;
        this.handler = handler;
        this.puller = new CommonPuller<>(handler);

        this.queues = new ConcurrentHashMap<>();
        this.puller.setQueues(queues.values());
        new Thread(puller, "Thread-Pull-" + name).start();

        // 状态监控任务
        if (monitor == null) {
            final long initMills = RandomUtil.rand(5000, 10000);
            monitor = IExecutor.build(name, 1, 8, 1);
            monitor.submit0Delay(new BaseDelayTask(initMills, 10 * 1000) {
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
    }

    /**
     * <pre>
     * 注册队列
     * </pre>
     */
    public void register(Object obj, CommonPullQueue<E> queue) {
        if (!queues.containsKey(obj)) {
            queue.setPuller(puller);
            queue.setName(name);
            queues.put(obj, queue);
        }
    }

    /**
     * <pre>
     * 移除队列
     * </pre>
     */
    public CommonPullQueue<E> remove(Object obj) {
        CommonPullQueue<E> queue = queues.remove(obj);
        final List<E> leftE = new ArrayList<>();
        queue.drain(leftE::add);
        handler.handle(leftE);
        return queue;
    }

    /**
     * <pre>
     * 暂停
     * </pre>
     */
    public void pause(Object obj) {
        CommonPullQueue<E> queue = queues.get(obj);
        if (queue != null) {
            queue.pausePoll();
        }
    }

    /**
     * <pre>
     * 继续
     * </pre>
     */
    public void resume(Object obj) {
        CommonPullQueue<E> queue = queues.get(obj);
        if (queue != null) {
            queue.resumePoll();
        }
    }

    /**
     * <pre>
     * 提交数据
     * ！先注册才能使用
     * </pre>
     */
    public boolean submit(Object obj, E e) {
        CommonPullQueue<E> queue = queues.get(obj);
        if (queue != null) {
            queue.submit(e);
            return true;
        }
        return false;
    }

    private void monitor() {
        for (CommonPullQueue<E> queue : queues.values()) {
            queue.printStatus();
        }
    }

    public void submitMonitor(BaseDelayTask delayTask) {
        monitor.submit0Delay(delayTask);
    }

    public void shutdown() {
        puller.shutdown();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": queueSize:" + queues.size();
    }

}
