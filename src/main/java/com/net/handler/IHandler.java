package com.net.handler;

import java.util.List;

/**
 * <pre>
 * 处理handle
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-28 15:03
 */
public interface IHandler<E extends IElm> {

    void handle(final E e);

    void handle(final List<E> list);

    void incrCount(int add);
}
