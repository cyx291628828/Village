package com.net.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.game.utils.JsonUtil;
import com.game.utils.serialize.ISerialize;
import com.net.channel.ChannelMgr;
import com.net.handler.IElm;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * <pre>
 * 数据包装
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-31 10:20
 */
public class PacketItem implements IElm {
    @JSONField(serialize = false)
    private byte[] bytes;
    private long userId;
    private short code;
    private long id;

    public PacketItem(long userId, short code) {
        this.id = 0;// TODO
        this.userId = userId;
        this.code = code;
    }

    public PacketItem encode(Object data) {
        if (bytes == null) {
            bytes = ISerialize.INSTANCE.encode(data);
        }
        return this;
    }

    ByteBuf build(Channel channel) {
        return PacketFactory.clientPacket(channel, code, bytes);
    }

    public PacketItem setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public long getUserId() {
        return userId;
    }

    public short getCode() {
        return code;
    }

    public long getId() {
        return id;
    }

    public void discard() {
        ChannelMgr.INSTANCE.getOpsMonitor().addTotalDCt(1);
    }

    @Override
    public String toString() {
        return JsonUtil.stringify(this);
    }


}
