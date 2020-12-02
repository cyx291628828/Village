package com.game.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * JSON工具类
 * </pre>
 *
 * @author yuxuan
 */
public class JsonUtil {

    static {
        int pfeatures = 0;
        pfeatures |= Feature.UseBigDecimal.getMask();
        pfeatures |= Feature.IgnoreNotMatch.getMask();
        pfeatures |= Feature.AutoCloseSource.getMask();
        pfeatures |= Feature.InternFieldNames.getMask();
        pfeatures |= Feature.SortFeidFastMatch.getMask();
        pfeatures |= Feature.AllowSingleQuotes.getMask();
        pfeatures |= Feature.AllowArbitraryCommas.getMask();
        pfeatures |= Feature.NonStringKeyAsString.getMask();
        pfeatures |= Feature.AllowUnQuotedFieldNames.getMask();
        pfeatures |= Feature.AllowISO8601DateFormat.getMask();
        JSON.DEFAULT_PARSER_FEATURE = pfeatures;


        int gfeatures = 0;
        gfeatures |= SerializerFeature.SortField.getMask();
        gfeatures |= SerializerFeature.QuoteFieldNames.getMask();
        gfeatures |= SerializerFeature.SkipTransientField.getMask();
        gfeatures |= SerializerFeature.WriteEnumUsingName.getMask();
        gfeatures |= SerializerFeature.UseISO8601DateFormat.getMask();
        gfeatures |= SerializerFeature.WriteNonStringKeyAsString.getMask();
//        gfeatures |= SerializerFeature.DisableCircularReferenceDetect.getMask();

        JSON.DEFAULT_GENERATE_FEATURE = gfeatures;
    }


    /**
     * <pre>
     * 对象序列化为json字符串
     * </pre>
     */
    public final static <T> String stringify(T t) {
        String json = "{}";
        try {
            json = JSON.toJSONString(t);
        } catch (Exception e) {
            Log.error("", e);
        }
        return json;
    }

    /**
     * <pre>
     * 对象序列化为二进制json
     * </pre>
     */
    public final static <T> byte[] binaryify(T t) {
        byte[] json = null;
        try {
            json = JSON.toJSONBytes(t);
        } catch (Exception e) {
            Log.error("", e);
        }
        return json;
    }


    /**
     * <pre>
     * 字符串反序列化为对象
     * </pre>
     */
    public static JSONObject parseObj(String str) {
        JSONObject obj = null;
        try {
            obj = (JSONObject) JSON.parse(str);
        } catch (Exception e) {
            Log.error("集合反序列化错误,jsonStr：" + str, e);
        }
        return obj;
    }

    /**
     * <pre>
     * 字符串反序列化为数组
     * </pre>
     */
    public static JSONArray parseArr(String str) {
        JSONArray array = null;
        try {
            array = (JSONArray) JSON.parse(str);
        } catch (Exception e) {
            Log.error("集合反序列化错误,jsonStr：" + str, e);
        }
        return array;
    }


    /**
     * <pre>
     * 二进制反序列化为对象
     * </pre>
     */
    public static JSONObject parseObj(byte[] bytes) {
        JSONObject obj = null;
        try {
            obj = (JSONObject) JSON.parse(bytes);
        } catch (Exception e) {
            Log.error("集合反序列化错误,jsonStr：" + bytes, e);
        }
        return obj;
    }

    /**
     * <pre>
     * 二进制反序列化为数组
     * </pre>
     */
    public static JSONArray parseArr(byte[] bytes) {
        JSONArray array = null;
        try {
            array = (JSONArray) JSON.parse(bytes);
        } catch (Exception e) {
            Log.error("集合反序列化错误,jsonStr：" + bytes, e);
        }
        return array;
    }

    private static void testCircular() {
        JSONObject obj1 = new JSONObject();
        Map obj2 = new HashMap();
        obj1.put("a", 1);
        obj1.put("t", obj2);
        obj2.put(2, 2);
        obj2.put("t", obj1);
        System.err.println(JSON.toJSONString(obj2));
    }

    public static void main(String[] args) {
        testCircular();
//        System.err.println(stringify(101));
//        System.err.println(stringify(new Date()));
    }
}
