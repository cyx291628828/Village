package com.net.common;

import com.game.utils.JsonUtil;
import com.game.utils.Log;
import org.jctools.queues.MpmcArrayQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * 包抛弃统计
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-03 16:58
 */
public class DiscardMonitor {

    private final static int MAX = 10;

    private String name;
    private AtomicLong disdCount;
    private MpmcArrayQueue<String> discardMsgs;

    public DiscardMonitor() {
        this(null);
    }

    public DiscardMonitor(String name) {
        this.name = name;
        this.disdCount = new AtomicLong();
        this.discardMsgs = new MpmcArrayQueue<>(10);
    }

    public void incrDisdCount() {
        this.disdCount.incrementAndGet();
    }

    public void addDisdMsg(String msg) {
        this.disdCount.incrementAndGet();
        this.discardMsgs.relaxedOffer(msg);
        while (this.discardMsgs.size() > MAX) {
            this.discardMsgs.poll();
        }
    }

    public void printCount() {
        long tempCount = disdCount.get();
        if (tempCount > 0) {
            discardMsgs.clear();
            disdCount.set(0);
            Log.warn("DiscardMonitor:" + name + " discard:" + tempCount);
        }
    }

    public void printMsgs() {
        long tempCount = disdCount.get();
        if (tempCount < 1) {
            return;
        }
        if (discardMsgs.isEmpty()) {
            return;
        }
        List<String> temps = new ArrayList<>(discardMsgs);
        discardMsgs.clear();
        disdCount.set(0);
        Log.warn("DiscardMonitor:" + name + " discard:" + tempCount + " " + JsonUtil.stringify(temps));
    }

    public void setName(String name) {
        this.name = name;
    }
}
