package com.net.common;

import com.game.utils.Log;
import com.net.handler.IElm;
import org.jctools.queues.MessagePassingQueue.Consumer;
import org.jctools.queues.MpmcArrayQueue;

/**
 * <pre>
 * 通用Pull队列
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-31 17:44
 */
public class CommonPullQueue<E extends IElm> {

    private DiscardMonitor discardMonitor;
    private volatile boolean pauseSubmit;
    private volatile boolean pausePull;
    private MpmcArrayQueue<E> queue;
    private CommonPuller<E> puller;

    private String name;
    private int halfCap;


    public CommonPullQueue(final int capacity) {
        this.halfCap = capacity;
        this.queue = new MpmcArrayQueue<>(capacity * 2);
        this.discardMonitor = new DiscardMonitor();
    }

    void pausePoll() {
        this.pausePull = true;
    }

    void resumePoll() {
        this.pausePull = false;
        this.puller.resume();
    }

    void pauseSubmit() {
        this.pauseSubmit = true;
    }

    void resumeSubmit() {
        this.pauseSubmit = false;
    }

    public void submit(E e) {
        if (pauseSubmit) {
            return;
        }
        if (!queue.offer(e)) {
            Log.error("QueueFull name:" + name + ",halfCap:" + halfCap);
            return;
        }
        puller.resume();
    }

    public E poll() {
        for (int i = 0, size = queue.size(); i < 10 && size > halfCap; i++) {
            E rmOne = queue.relaxedPoll();
            if (rmOne != null) {
                discardMonitor.incrDisdCount();
                rmOne.discard();
                size--;
            }
        }
        if (pausePull) {
            return null;
        }
        return queue.poll();
    }

    public void drain(Consumer<E> consumer) {
        queue.drain(consumer);
    }


    void setPuller(CommonPuller<E> puller) {
        this.puller = puller;
    }

    public void setName(String name) {
        this.discardMonitor.setName(name);
        this.name = name;
    }

    public void printStatus() {
        final int size = queue.size();
        if (size > 5) {
            Log.info("QMonitor-" + name + ":" + size + "/" + halfCap);
        }
        this.discardMonitor.printCount();
    }

    @Override
    public String toString() {
        return "PacketQueue:" + name + ",halfCap:" + halfCap;
    }
}
