package com.game.utils.serialize.imp;

import com.game.utils.serialize.BaseSerialize;
import com.game.utils.serialize.ISerialize;
import io.protostuff.MsgpackIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * <pre>
 * ProtoMsgPack序列化
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public class ProtoMsgPackUtil extends BaseSerialize {

    public final static ISerialize INSTANCE = new ProtoMsgPackUtil();


    private ProtoMsgPackUtil() {
    }

    @Override
    protected <T> byte[] doEncode(T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        return MsgpackIOUtil.toByteArray(t, schema, false);
    }

    @Override
    protected <T> T doDecode(byte[] bytes, T t) throws Exception {
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        MsgpackIOUtil.mergeFrom(bytes, t, schema, false);
        return t;
    }

    @Override
    protected <T> T doEndecode(T t, Class<T> clazz) throws Exception {
        Schema schema = RuntimeSchema.getSchema(clazz);
        MsgpackIOUtil.mergeFrom(MsgpackIOUtil.toByteArray(t, schema, false), t, schema, false);
        return t;
    }

}
