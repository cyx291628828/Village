package com.game.utils.serialize;

import com.game.utils.serialize.imp.ProtoBuffUtil;

/**
 * <pre>
 * 序列化
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public interface ISerialize {

    /**
     * <pre>
     * 确定使用哪个编码器
     * </pre>
     */
    ISerialize INSTANCE = ProtoBuffUtil.INSTANCE;


    /**
     * <pre>
     * 编码
     * </pre>
     */
    <T> byte[] encode(T t);


    /**
     * <pre>
     * 解码
     * </pre>
     */
    <T> T decode(byte[] bytes, Class<T> clazz);


    /**
     * <pre>
     * 解码
     * </pre>
     */
    <T> T decode(byte[] bytes, T t);


    /**
     * <pre>
     * 编解码
     * 一般只用于benchmark
     * </pre>
     */
    <T> T endecode(T t, Class<T> clazz);

}
