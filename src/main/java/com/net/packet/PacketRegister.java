package com.net.packet;

/**
 * <pre>
 * 连接注册信息
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-26 14:50
 */
public class PacketRegister {

    // 验证key
    private String key;

    // 地址
    private long addr;

    // 负载修正
    private int load;


    public PacketRegister() {
    }

    public String getKey() {
        return key;
    }

    public PacketRegister setKey(String key) {
        this.key = key;
        return this;
    }

    public long getAddr() {
        return addr;
    }

    public void setAddr(long addr) {
        this.addr = addr;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "PacketRegister{" +
                "addr=" + addr +
                ", load=" + load +
                '}';
    }

}
