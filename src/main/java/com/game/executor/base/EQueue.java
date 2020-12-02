package com.game.executor.base;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 执行队列-顶层接口
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-26 19:53
 */
public interface EQueue<E> {

    void submit(E task);

    E poll(long timeout, TimeUnit unit) throws InterruptedException;

    E poll();

    void next(E last);

    void printStatus();

    void setPuller(IPull puller);
}
