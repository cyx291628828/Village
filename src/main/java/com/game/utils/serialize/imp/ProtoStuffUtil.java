package com.game.utils.serialize.imp;

import com.game.utils.serialize.BaseSerialize;
import com.game.utils.serialize.ISerialize;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * <pre>
 * ProtoStuff序列化
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public class ProtoStuffUtil extends BaseSerialize {

    public final static ISerialize INSTANCE = new ProtoStuffUtil();


    private ProtoStuffUtil() {
    }

    @Override
    protected <T> byte[] doEncode(T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        return ProtostuffIOUtil.toByteArray(t, schema, LinkedBuffer.allocate());
    }

    @Override
    protected <T> T doDecode(byte[] bytes, T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        ProtostuffIOUtil.mergeFrom(bytes, t, schema);
        return t;
    }

    @Override
    protected <T> T doEndecode(T t, Class<T> clazz) throws Exception {
        Schema schema = RuntimeSchema.getSchema(clazz);
        ProtostuffIOUtil.mergeFrom(ProtostuffIOUtil.toByteArray(t, schema, LinkedBuffer.allocate()), t, schema);
        return t;
    }

}
