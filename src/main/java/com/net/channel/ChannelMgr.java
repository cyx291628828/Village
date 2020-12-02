package com.net.channel;

import com.game.executor.task.BaseDelayTask;
import com.game.registry.BaseRegistry;
import com.game.utils.ByteBufUtil;
import com.game.utils.JsonUtil;
import com.game.utils.Log;
import com.game.utils.RandomUtil;
import com.game.utils.serialize.ISerialize;
import com.net.code.ICodeFilters;
import com.net.common.CommonExecutor;
import com.net.common.CommonPullQueue;
import com.net.handler.IHandler;
import com.net.packet.OpsMonitor;
import com.net.packet.PacketHandler;
import com.net.packet.PacketItem;
import io.netty.channel.Channel;

import java.util.List;


/**
 * <pre>
 * 连接管理
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-08 18:07
 */
public class ChannelMgr<K, V> extends BaseRegistry<Long, Channel> {

    public final static ChannelMgr<Long, Channel> INSTANCE = new ChannelMgr<>();
    private final static int QUEUE_CAPACITY = 256;

    private CommonExecutor<PacketItem> executor;
    private ICodeFilters codeFilters;
    private OpsMonitor opsMonitor;


    public void init(ICodeFilters codeFilters) {
        final String name = "PacketExecutor";
        this.opsMonitor = new OpsMonitor(name);

        IHandler<PacketItem> handler = new PacketHandler<>(opsMonitor);
        this.executor = new CommonExecutor<>(name, handler);

        final long initMills = RandomUtil.rand(5000, 10000);
        this.executor.submitMonitor(new BaseDelayTask(initMills, 100 * 1000) {
            @Override
            protected void exec() throws Exception {
                opsMonitor.fresh();
                Log.info(opsMonitor.getMsgs());
            }

            @Override
            protected String getName() {
                return "Ops-" + name;
            }
        });
        this.codeFilters = codeFilters;
    }

    @Override
    public Channel put(Long userId, Channel channel) {
        executor.register(userId, new CommonPullQueue<>(QUEUE_CAPACITY));
        return super.put(userId, channel);
    }

    @Override
    public Channel remove(Long userId) {
        Channel channel = super.remove(userId);
        if (channel != null) {
            executor.remove(userId);
        }
        return channel;
    }

    /**
     * <pre>
     * Channel可写状态改变
     * </pre>
     */
    public void changeWritable(Long userId, boolean writable) {
        if (writable) {
            executor.resume(userId);
        } else {
            executor.pause(userId);
        }
    }

    /**
     * <pre>
     * 提交发送(直接发送)
     * </pre>
     */
    public void submit(long userId, short code, Object data) {
        if (data == null) {
            return;
        }
        Channel channel = get(userId);
        if (!ByteBufUtil.isChannelActive(channel)) {
            return;
        }
        PacketItem packetItem = new PacketItem(userId, code).encode(data);
        if (!ByteBufUtil.checkLength(code, packetItem.getBytes())) {
            return;
        }
        if (executor.submit(userId, packetItem)) {
            opsMonitor.addTotalWCt(1);
            if (codeFilters.isResLog(code)) {
                Log.info("--------sendClient code:" + code + ",userId:" + userId + ",data:" + JsonUtil.stringify(data));
            }
        }

    }

    /**
     * <pre>
     * 提交广播(直接发送)
     * </pre>
     */
    public void broadCast(List<Long> userIds, short code, Object data) {
        if (data == null || userIds.isEmpty()) {
            return;
        }
        byte[] bytes = ISerialize.INSTANCE.encode(data);
        if (!ByteBufUtil.checkLength(code, bytes)) {
            return;
        }
        for (Long userId : userIds) {
            Channel channel = get(userId);
            if (!ByteBufUtil.isChannelActive(channel)) {
                continue;
            }
            PacketItem packetItem = new PacketItem(userId, code).setBytes(bytes);
            if (executor.submit(userId, packetItem)) {
                opsMonitor.addTotalWCt(1);
            }
        }
        if (codeFilters.isResLog(code)) {
            Log.info("-------------broadCast,code:" + code + ",userIds:" + JsonUtil.stringify(userIds) + ",data:" + JsonUtil.stringify(data));
        }
//        else {
//            Log.info("_s_:" + code + "," + JsonUtil.stringify(userIds));
//        }
    }

    public OpsMonitor getOpsMonitor() {
        return opsMonitor;
    }

}
