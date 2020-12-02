package com.net.common;

import com.game.executor.wait.IWaitFor;
import com.game.executor.wait.IWaitForStage;
import com.game.utils.Log;
import com.net.handler.IElm;
import com.net.handler.IHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 拉取执行
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-31 16:34
 */
public class CommonPuller<E extends IElm> implements Runnable {

    private Collection<CommonPullQueue<E>> queues;
    private volatile boolean running;
    private IHandler<E> handler;
    private IWaitFor waiter;

    CommonPuller(IHandler<E> handler) {
        this.waiter = new IWaitForStage(1000L);
//        this.waiter = new IWaitForYield();
        this.handler = handler;
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
            for (CommonPullQueue<E> queue : queues) {
                try {
                    E e1 = queue.poll();
                    if (e1 == null) {
                        continue;
                    }
                    haveTask = true;

                    E e2 = queue.poll();
                    if (e2 != null) {
                        List<E> list = new ArrayList<>();
                        list.add(e1);
                        list.add(e2);
                        int calc = 2;
                        while ((e2 = queue.poll()) != null && ++calc <= 8) {
                            list.add(e2);
                        }
                        handler.handle(list);
                        continue;
                    }
                    handler.handle(e1);
                } catch (Exception e) {
                    Log.error("", e);
                }
            }
            if (!haveTask) {
                emptyLoop++;
            }
        }
    }


    public void setQueues(Collection<CommonPullQueue<E>> queues) {
        this.queues = queues;
    }

    void resume() {
        this.waiter.signalAll();
    }

    public void shutdown() {
        this.running = false;
    }

}
