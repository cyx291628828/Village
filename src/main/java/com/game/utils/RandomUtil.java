package com.game.utils;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <pre>
 * 随机数工具类
 * </pre>
 *
 * @author yuxuan
 */
public final class RandomUtil {

    /**
     * <pre>
     * 获取随机数
     * </pre>
     *
     * @param min 包含
     * @param max 包含
     */
    public static int rand(int min, int max) {
        if (min >= max) {
            return max;
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * <pre>
     * 0->max随机数
     * 本机此刻：1000W次38毫秒
     * </pre>
     *
     * @param max 包含
     */
    public static int rand(int max) {
        return rand(0, max);
    }

    /**
     * <pre>
     * 1->100(包含)随机数
     * </pre>
     */
    public static int rand100() {
        return rand(1, 101);
    }

    /**
     * <pre>
     * 1->1000(包含)随机数
     * </pre>
     */
    public static int rand1000() {
        return rand(1, 1001);
    }

    /**
     * <pre>
     * 生成一定范围的不重复随机数组
     * 本机此刻：1000万次，10位，501ms左右
     * </pre>
     *
     * @param min 包含
     * @param max 包含
     */
    public static int[] randomArray(int min, int max) {
        return randomArray(min, max, 0);
    }

    /**
     * <pre>
     * 生成一定范围的不重复随机数组
     * </pre>
     *
     * @param min   包含
     * @param max   包含
     * @param count 数组长度(<=max-min+1)
     */
    public static int[] randomArray(int min, int max, int count) {
        if (min >= max) {
            return new int[]{min};
        }
        if (min < 0) {
            return new int[]{0};
        }
        int range = max - min + 1;
        if (count == 0) {
            count = range;
        }
        // 最大长度
        if (count > range) {
            count = range;
        }
        int[] rsc = new int[range];
        for (int i = 0; i < range; i++) {
            rsc[i] = i + min;// 填充
        }
        int ran, temp, endIndex;
        int[] result = new int[range];
        for (int i = 0; i < range; i++) {
            endIndex = range - i - 1;
            if (endIndex > 0) {
                ran = rand(endIndex);
            } else {
                ran = 0;
            }
            temp = rsc[ran];// 生成
            result[i] = temp;
            rsc[ran] = rsc[endIndex];// 交换
            rsc[endIndex] = temp;
        }
        if (count < range) {
            return Arrays.copyOf(result, count);
        }
        return result;
    }

    public static void main(String[] args) {
//        long st = System.currentTimeMillis();
//        int[] nums = new int[10];
//        for (int i = 0; i < 10000000; i++) {
//            int randNum = rand(1, 10);
//            nums[randNum - 1]++;
//            nums = randomArray(1, 10, 5);
//        }
//        long ct = System.currentTimeMillis() - st;
//        System.out.println("ct:" + ct + Arrays.toString(nums));
        for (int i = 0; i < 10; i++) {
//            System.out.println(Arrays.toString(randomArray(0, 1)));
            System.out.println(Arrays.toString(randomArray(0, 10)));
        }
    }

}
