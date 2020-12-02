package com.game.utils.serialize.imp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.utils.serialize.BaseSerialize;
import com.game.utils.serialize.ISerialize;
import org.msgpack.jackson.dataformat.MessagePackFactory;

/**
 * <pre>
 * MessagePack序列化
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-24 17:45
 */
public class MessagePackUtil extends BaseSerialize {

    public final static ISerialize INSTANCE = new MessagePackUtil();

    private ObjectMapper objectMapper;

    private MessagePackUtil() {
        super();
        this.objectMapper = new ObjectMapper(new MessagePackFactory());
    }

    @Override
    protected <T> byte[] doEncode(T t) throws Exception {
        return objectMapper.writeValueAsBytes(t);
    }

    /**
     * <pre>
     * 反序列化
     * ！二进制中多了字段会报错，少了字段取默认值
     * </pre>
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doDecode(byte[] bytes, T t) throws Exception {
        return (T) objectMapper.readValue(bytes, t.getClass());
    }

    @Override
    protected <T> T doEndecode(T t, Class<T> clazz) throws Exception {
        return objectMapper.readValue(objectMapper.writeValueAsBytes(t), clazz);
    }


}
