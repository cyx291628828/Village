package com.game.utils.serialize.imp;

import com.game.utils.serialize.BaseSerialize;
import com.game.utils.serialize.ISerialize;
import io.protostuff.JsonIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * <pre>
 * ProtoJson序列化
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public class ProtoJsonUtil extends BaseSerialize {

    public final static ISerialize INSTANCE = new ProtoJsonUtil();


    private ProtoJsonUtil() {
    }

    @Override
    protected <T> byte[] doEncode(T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        return JsonIOUtil.toByteArray(t, schema, false);
    }

    @Override
    protected <T> T doDecode(byte[] bytes, T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        JsonIOUtil.mergeFrom(bytes, t, schema, false);
        return t;
    }

    @Override
    protected <T> T doEndecode(T t, Class<T> clazz) throws Exception {
        Schema schema = RuntimeSchema.getSchema(clazz);
        JsonIOUtil.mergeFrom(JsonIOUtil.toByteArray(t, schema, false), t, schema, false);
        return t;
    }

}
