package com.net.packet;


import com.game.utils.JsonUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * ops统计
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-02 20:34
 */
public class OpsMonitor {


    private final static int MAX = 10; // 最大保留个数

    private BlockingQueue<Long> totalRecvOps;// 收包速率
    private BlockingQueue<Long> totalSendOps;// 发包速率
    private AtomicLong totalDCt;// 超载丢弃数
    private AtomicLong totalWCt;// 写入队列数
    private AtomicLong totalRCt;// 网络收包数
    private AtomicLong totalSCt;// 网络发包数
    private String name;// 名称
    private String msgs;// 信息

    public OpsMonitor(String name) {
        this.totalRecvOps = new ArrayBlockingQueue<>(MAX * 2);
        this.totalSendOps = new ArrayBlockingQueue<>(MAX * 2);
        this.totalRCt = new AtomicLong();
        this.totalSCt = new AtomicLong();
        this.totalWCt = new AtomicLong();
        this.totalDCt = new AtomicLong();
        this.name = name;
    }

    public long getTotalDCt() {
        return totalDCt.get();
    }

    public void addTotalDCt(long recvCount) {
        this.totalDCt.getAndAdd(recvCount);
    }

    public long getTotalWCt() {
        return totalWCt.get();
    }

    public void addTotalWCt(long sendCount) {
        this.totalWCt.getAndAdd(sendCount);
    }

    public long getTotalRCt() {
        return totalRCt.get();
    }

    public void addTotalRCt(long totalRCt) {
        this.totalRCt.getAndAdd(totalRCt);
    }

    public long getTotalSCt() {
        return totalSCt.get();
    }

    public void addTotalSCt(long totalSCt) {
        this.totalSCt.getAndAdd(totalSCt);
    }


    /**
     * <pre>
     * 刷新并记录
     * ！决定统计频率
     * </pre>
     */
    public void fresh() {
        this.totalRecvOps.offer(totalRCt.get());
        while (this.totalRecvOps.size() > MAX) {
            this.totalRecvOps.poll();
        }
        this.totalSendOps.offer(totalSCt.get());
        while (this.totalSendOps.size() > MAX) {
            this.totalSendOps.poll();
        }
        msgs = buildMsgs();

        this.totalRCt.set(0);
        this.totalDCt.set(0);
        this.totalSCt.set(0);
        this.totalWCt.set(0);
    }

    private String buildMsgs() {
        return "----------OpsMonitor-" + name + "----------------\n" +
                "totalRCt:" + totalRCt.get() + ",totalWCt:" + totalWCt.get() + ",recvOps:" + JsonUtil.stringify(totalRecvOps) + "\n" +
                "totalDCt:" + totalDCt.get() + ",totalSCt:" + totalSCt.get() + ",sendOps:" + JsonUtil.stringify(totalSendOps);
    }

    public String getMsgs() {
        return msgs;
    }

    public static void main(String[] args) {
        OpsMonitor opsMonitor = new OpsMonitor("Test");
        opsMonitor.addTotalRCt(123);
        opsMonitor.addTotalDCt(88);
        opsMonitor.addTotalSCt(456);
        opsMonitor.addTotalWCt(188);
        opsMonitor.fresh();
        opsMonitor.addTotalRCt(123);
        opsMonitor.addTotalDCt(88);
        opsMonitor.addTotalSCt(456);
        opsMonitor.addTotalWCt(188);
        opsMonitor.fresh();
        opsMonitor.addTotalRCt(123);
        opsMonitor.addTotalDCt(88);
        opsMonitor.addTotalSCt(456);
        opsMonitor.addTotalWCt(188);
        System.err.println(opsMonitor.buildMsgs());
    }

}
