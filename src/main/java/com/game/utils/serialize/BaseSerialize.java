package com.game.utils.serialize;

import com.game.utils.Log;

/**
 * <pre>
 * 序列化
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public abstract class BaseSerialize implements ISerialize {

    public BaseSerialize() {
    }

    @Override
    public <T> byte[] encode(T t) {
        try {
            return doEncode(t);
        } catch (Exception e) {
            Log.error("", e);
        }
        return null;
    }

    protected abstract <T> byte[] doEncode(T t) throws Exception;

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            return doDecode(bytes, t);
        } catch (Exception e) {
            Log.error("", e);
        }
        return null;
    }

    @Override
    public <T> T decode(byte[] bytes, T t) {
        try {
            return doDecode(bytes, t);
        } catch (Exception e) {
            Log.error("", e);
        }
        return null;
    }

    @Override
    public <T> T endecode(T t, Class<T> clazz) {
        try {
            return doEndecode(t, clazz);
        } catch (Exception e) {
            Log.error("", e);
        }
        return null;
    }

    protected abstract <T> T doDecode(byte[] bytes, T t) throws Exception;

    protected abstract <T> T doEndecode(T t, Class<T> clazz) throws Exception;

}
