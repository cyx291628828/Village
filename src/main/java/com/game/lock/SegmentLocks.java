package com.game.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <pre>
 * 阶段性锁管理
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-23 17:19
 */
public class SegmentLocks {

    private int size;
    private ReentrantReadWriteLock[] locks;

    public SegmentLocks(int size) {
        this.locks = new ReentrantReadWriteLock[size];
        this.size = size;
        this.init();
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            locks[i] = new ReentrantReadWriteLock();
        }
    }

    public Lock writeLock(long id) {
        int temp = (int) id;
        if (temp < 0) {
            temp = -1 * temp;
        }
        return locks[temp % size].writeLock();
    }

    public Lock readLock(long id) {
        int temp = (int) id;
        if (temp < 0) {
            temp = -1 * temp;
        }
        return locks[temp % size].readLock();
    }

    public static void main(String[] args) {
        System.err.println((int) 12313465413213214L % 8);
        System.err.println((int) 1212365413213215636L % 8);
        System.err.println((int) 1231123354113212323L % 8);
    }

}
