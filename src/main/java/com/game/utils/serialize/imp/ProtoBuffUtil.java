package com.game.utils.serialize.imp;

import com.game.utils.serialize.BaseSerialize;
import com.game.utils.serialize.ISerialize;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * <pre>
 * ProtoBuff序列化
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public class ProtoBuffUtil extends BaseSerialize {

    public final static ISerialize INSTANCE = new ProtoBuffUtil();


    private ProtoBuffUtil() {
    }

    @Override
    protected <T> byte[] doEncode(T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        return ProtobufIOUtil.toByteArray(t, schema, LinkedBuffer.allocate());
    }

    @Override
    protected <T> T doDecode(byte[] bytes, T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        ProtobufIOUtil.mergeFrom(bytes, t, schema);
        return t;
    }

    @Override
    protected <T> T doEndecode(T t, Class<T> clazz) throws Exception {
        Schema schema = RuntimeSchema.getSchema(clazz);
        ProtobufIOUtil.mergeFrom(ProtobufIOUtil.toByteArray(t, schema, LinkedBuffer.allocate()), t, schema);
        return t;
    }

}
