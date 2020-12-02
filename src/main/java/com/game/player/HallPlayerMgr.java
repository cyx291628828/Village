package com.game.player;

import com.game.lock.SegmentLocks;
import com.game.registry.ExecutorRegistry;

import java.util.concurrent.locks.Lock;

/**
 * <pre>
 * 大厅玩家管理
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-06 20:31
 */
public class HallPlayerMgr<K, V> extends ExecutorRegistry<Long, HallPlayer> {

    public final static HallPlayerMgr<Long, HallPlayer> INSTANCE = new HallPlayerMgr<>();

    private SegmentLocks locks;

    private HallPlayerMgr() {
        this.locks = new SegmentLocks(8);
    }

    @Override
    public void init(String name, int threadCount, int capacity, int type) throws Exception {
        super.init(name, threadCount, capacity, type);
    }

    /**
     * <pre>
     * 定时扫描
     * </pre>
     */
    public void scan() {
        for (HallPlayer player : getInstancePool().values()) {
        }

    }

    @Override
    public HallPlayer put(Long key, HallPlayer value) {
        return super.put(key, value);
    }

    @Override
    public HallPlayer get(Long userId) {
        if (userId == null) {
            return null;
        }
        HallPlayer player = super.get(userId);
        if (player != null) {
            return player;
        }
        Lock lock = locks.writeLock(userId);
        try {
            lock.lock();
            player = super.get(userId);
            if (player != null) {
                return player;
            }
            player = new HallPlayer(userId);
            put(userId, player);
        } finally {
            lock.unlock();
        }
        return player;
    }

    @Override
    public HallPlayer remove(Long key) {
        return super.remove(key);
    }

    /**
     * <pre>
     * 发送客户端
     * </pre>
     */
    public void sendClient(long userId, short code, Object data) {
        HallPlayer player = super.get(userId);
        if (player == null) {
            return;
        }
    }

    /**
     * <pre>
     * 下线事件
     * </pre>
     */
    public void logOff(final long userId) {
        HallPlayer player = get(userId);
        if (player == null) {
            return;
        }
    }

    /**
     * <pre>
     * 停服
     * </pre>
     */
    public void shutdown() {
        // 发送下线提示,在future中断开连接(自动触发下线事件)

        // 关闭线程池
        super.shutdown();
    }

}
