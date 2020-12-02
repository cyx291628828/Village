package com.game.utils;

/**
 * <pre>
 * 字符串工具
 * </pre>
 *
 * @author yuxuan
 */
public class StringUtil {


    /**
     * <pre>
     * 字符串空判断
     * </pre>
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }


    /**
     * <pre>
     * 字符串某字母转大写
     * </pre>
     */
    public static String toUpperCaseByIndex(String str, int index) {
        char[] chArr = str.toCharArray();
        chArr[index] = toUpperCase(chArr[index]);
        return String.valueOf(chArr);
    }

    /**
     * <pre>
     * 字符串某字母转小写
     * </pre>
     */
    public static String toLowerCaseByIndex(String str, int index) {
        if (isEmpty(str)) {
            return str;
        }
        char[] chArr = str.toCharArray();
        chArr[index] = toLowerCase(chArr[index]);
        return String.valueOf(chArr);
    }

    /**
     * <pre>
     * 字符转成大写
     * </pre>
     */
    private static char toUpperCase(char chat) {
        if (97 <= chat && chat <= 122) {
            chat ^= 32;
        }
        return chat;
    }

    /**
     * <pre>
     * 字符转成小写
     * </pre>
     */
    private static char toLowerCase(char chat) {
        if (65 <= chat && chat <= 90) {
            chat |= 32;
        }
        return chat;
    }


    /**
     * <pre>
     * 左填充字符
     * </pre>
     */
    public static String leftPad(String str, char pad, Integer len) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < len) {
            sb.insert(0, pad);
        }
        return sb.toString();
    }


    public static void main(String[] args) {

    }

}
