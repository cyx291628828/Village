package com.game.executor.base;

/**
 * <pre>
 * 拉取执行
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-26 17:01
 */
public interface IPull<E> extends Runnable {

    void setQueues(EQueue<E>[] queues);

    void resume();

    void shutdown();

}
