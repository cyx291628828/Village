package com.game.utils;

/**
 * <pre>
 * 二进制运算工具包
 * 最大只支持32位运算
 * pos参数不允许小于0或者大于31
 * </pre>
 */
public class BinaryUtil {

    /**
     * <pre>
     * 检测二进制的某一位是否为1
     * 位置从右往左开始
     * 如 00001101
     *   第0位是1
     *   第1位是0
     *   第2位是1
     *   ...
     * </pre>
     *
     * @param binary 二进制
     * @param pos    二进制位,从右往左的位数
     */
    public static boolean IsPos1(int binary, int pos) {
        paramCheck(pos);
        return getPosVal(binary, pos) == 1;
    }

    /**
     * <pre>
     * 选择性的将二进制的某位设置为1
     * 位置从右往左开始
     * 如  binary 参数为00000000
     * 	 pos参数为0,则结果为00000001
     * 	 pos参数为1,则结果为00000010
     * 	 pos参数为2,则结果为00000100
     * </pre>
     *
     * @param binary 二进制
     * @param pos    二进制位,从右往左的位数
     */
    public static int setPos1(int binary, int pos) {
        paramCheck(pos);
        return binary | (1 << pos);
    }

    /**
     * <pre>
     * 选择性的将二进制的某位设置为0
     * 位置从右往左开始
     * 如  binary 参数为11111111
     * 	 pos参数为0,则结果为11111110
     * 	 pos参数为1,则结果为11111101
     * 	 pos参数为2,则结果为11111011
     * </pre>
     *
     * @param binary 二进制
     * @param pos    二进制位,从右往左的位数
     */
    public static int setPos0(int binary, int pos) {
        paramCheck(pos);
        return binary & ~(1 << pos);
    }

    /**
     * <pre>
     * 检测二进制的某一位的值
     * 位置从右往左开始
     * 如 00001101
     *   第0位是1
     *   第1位是0
     *   第2位是1
     *   ...
     * </pre>
     *
     * @param binary 二进制
     * @param pos    二进制位,从右往左的位数
     */
    public static int getPosVal(int binary, int pos) {
        paramCheck(pos);
        return (binary >>> pos) & 1;
    }

    /**
     * <pre>
     * 参数检测
     * </pre>
     */
    private static void paramCheck(int pos) {
        if (pos > 31 || pos < 0) {
            throw new IllegalArgumentException("pos must >=0 and <=31");
        }
    }

    /**
     * <pre>
     * 转二进制1的个数
     * </pre>
     */
    public static int getCtsOf1(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);
            count++;
        }
        return count;
    }

    public static void main(String[] args) {

        System.out.println(getCtsOf1(57));
//         int pos = 6;
//        int binary = 321;
//         System.out.println(Integer.toBinaryString(binary));
//         System.out.println(getPosVal(binary, pos));
//         binary >>>= pos;
//         binary = getPosVal(binary, pos);
//         System.out.println(Integer.toBinaryString(binary));
//         binary = setPos1(binary, pos);
//         System.out.println(Integer.toBinaryString(binary));

        // binary = setPos1(binary, pos);
        // System.out.println(Integer.toBinaryString(binary));
        //
        int binary = 0x40;
        System.out.println(binary);
        binary = setPos1(binary, 15);
        System.out.println(binary);
        //
        binary = binary ^ (1 << (7 - 1));
        System.out.println(binary);

        System.out.println(getPosVal(5, 0));
    }
}
