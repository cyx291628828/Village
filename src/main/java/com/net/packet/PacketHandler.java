package com.net.packet;

import com.game.utils.ByteBufUtil;
import com.net.channel.ChannelMgr;
import com.net.handler.IHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.List;

/**
 * <pre>
 * 包处理
 * </pre>
 *
 * @author yuxuan
 * @time 2020-10-31 14:12
 */
public class PacketHandler<E extends PacketItem> implements IHandler<E> {

    private GenericFutureListener<Future<? super Void>> listener;
    private final OpsMonitor opsMonitor;

    public PacketHandler(OpsMonitor opsMonitor) {
        listener = future -> incrCount(1);
        this.opsMonitor = opsMonitor;
    }

    private ChannelPromise newPromise(Channel channel, final int count) {
        return channel.newPromise().addListener(future -> incrCount(count));
    }

    @Override
    public void handle(final E e) {
        Channel channel = ChannelMgr.INSTANCE.get(e.getUserId());
        if (!ByteBufUtil.isChannelActive(channel)) {
            return;
        }

        channel.eventLoop().submit(() -> {
            ByteBuf resBuf = e.build(channel);
            if (resBuf == null) {
                return;
            }
            channel.writeAndFlush(resBuf, channel.newPromise().addListener(listener));
        });

    }

    @Override
    public void handle(final List<E> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        final E e = list.get(0);
        final Channel channel = ChannelMgr.INSTANCE.get(e.getUserId());
        if (!ByteBufUtil.isChannelActive(channel)) {
            return;
        }

        channel.eventLoop().submit(() -> {
            ByteBuf resBuf = PacketFactory.clientPackets(channel, list);
            if (resBuf == null) {
                return;
            }
            channel.writeAndFlush(resBuf, newPromise(channel, list.size()));
        });
    }

    @Override
    public void incrCount(int add) {
//        if (add > 1) {
//            System.err.println("----------------- add:" + add);
//        }
        opsMonitor.addTotalSCt(add);
    }

}
