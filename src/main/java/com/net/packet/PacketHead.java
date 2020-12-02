package com.net.packet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.utils.BinaryUtil;
import com.game.utils.RandomStrUtil;

import java.util.List;

/**
 * <pre>
 * 头部信息
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-26 14:50
 */
public class PacketHead {

    /**
     * <pre>
     * 请求唯一Id
     * </pre>
     */
    private String qid;

    /**
     * <pre>
     * 来源地址
     * </pre>
     */
    private long src;

    /**
     * <pre>
     * 目标地址或者类型
     * </pre>
     */
    private long dest;

    /**
     * <pre>
     * 标志位：第0位是否回执包，第1位是否客户端包，第2位是否sproto编码...
     * </pre>
     */
    private short flag;


    private short code;

    private BroadInfo broad;

    public PacketHead() {
        this.qid = RandomStrUtil.randomStr(10);
    }

    public PacketHead(short code, long src) {
        this();
        this.code = code;
        this.src = src;
    }


    // 该构造器没有qid
    public PacketHead(boolean isClient, short code) {
        this.code = code;
        if (isClient) {
            this.setClient();
        }
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public long getSrc() {
        return src;
    }

    public void setSrc(long src) {
        this.src = src;
    }

    public long getDest() {
        return dest;
    }

    public PacketHead setDest(long dest) {
        this.dest = dest;
        return this;
    }

    public short getFlag() {
        return flag;
    }

    public void setFlag(short flag) {
        this.flag = flag;
    }

    @JsonIgnore
    public boolean isCallback() {
        return BinaryUtil.IsPos1(this.flag, 0);
    }

    public PacketHead setCallback() {
        this.flag = (short) BinaryUtil.setPos1(this.flag, 0);
        return this;
    }

    @JsonIgnore
    public boolean isClient() {
        return BinaryUtil.IsPos1(this.flag, 1);
    }

    public PacketHead setClient() {
        this.flag = (short) BinaryUtil.setPos1(this.flag, 1);
        return this;
    }

    @JsonIgnore
    public boolean isSproto() {
        return BinaryUtil.IsPos1(this.flag, 2);
    }

    public PacketHead setSproto() {
        this.flag = (short) BinaryUtil.setPos1(this.flag, 2);
        return this;
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public BroadInfo getBroad() {
        return broad;
    }

    public void setBroad(BroadInfo broad) {
        this.broad = broad;
    }

    @JsonIgnore
    public boolean check() {
        return code > 0 && src > 0 && qid != null;
    }

    @Override
    public String toString() {
        return "PacketHead{" +
                "qid='" + qid + '\'' +
                ", src=" + src +
                ", dest=" + dest +
                ", flag=" + Integer.toBinaryString(flag) +
                ", isCallback=" + isCallback() +
                ", code=" + code +
                ", broad=" + broad +
                '}';
    }

    public String basicDesc() {
        return
                " Head:{\"code\"=" + code +
                        ",\"qid\"=\"" + qid + "\"" +
                        ",\"src\"=" + src +
                        ",\"dest\"=" + dest +
                        ",\"flag\"=\"" + Integer.toBinaryString(flag) + "\"" +
                        ",\"isCB\"=" + isCallback() + "}"
                ;

    }

    private static class BroadInfo {

        /**
         * <pre>
         * 需广播的地址列表
         * </pre>
         */
        private List<Integer> bs;


        /**
         * <pre>
         * 需广播的类型，优先级比地址列表要低
         * </pre>
         */
        private int bt;


        public List<Integer> getBs() {
            return bs;
        }

        public void setBs(List<Integer> bs) {
            this.bs = bs;
        }

        public int getBt() {
            return bt;
        }

        public void setBt(int bt) {
            this.bt = bt;
        }

        @Override
        public String toString() {
            return "BroadInfo{" +
                    "bs=" + bs +
                    ", bt=" + bt +
                    '}';
        }
    }

}
