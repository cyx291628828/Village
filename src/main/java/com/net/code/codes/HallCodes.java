package com.net.code.codes;

/**
 * <pre>
 * 大厅协议号
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-01 11:55
 */
public interface HallCodes {

    //---------------------- c2s 1-5000 ----------------------------------
    /**
     * 内网创角或登录
     */
    short REGISTER = 1;

    /**
     * 英雄激活
     */
    short HERO_ACTIVATE = 11;

    /**
     * 新手训练进度
     */
    short TRAIN = 12;

    /**
     * 玩家主动请求模块数据
     */
    short REQUEST_SEND = 13;

    /**
     * 商品购买
     */
    short SHOP_BUY = 14;

    /**
     * 物品使用
     */
    short ITEM_USE = 15;

    /**
     * 完成任务
     */
    short FINISH_ASSIGN = 16;

    /**
     * 简单数据上报
     */
    short SIMPLE_COUNT = 17;

    //---------------------- c2s 1-5000 ----------------------------------
    /**
     * c2s最大值
     */
    short C2S_MAX = 5000;

    //---------------------- c2s 1-5000 ----------------------------------
    //---------------------- s2c 5001-10000 ------------------------------

    /**
     * 玩家基本信息
     */
    short PLAYER_DATA = 5001;

    /**
     * 英雄列表
     */
    short HERO_LIST = 5002;

    /**
     * 大厅通用数据,登录之后立马发送
     */
    short HALL_COMMON = 5003;

    /**
     * 大厅通用数据,登录之后不发送，需要主动请求
     */
    short HALL_COMMON_LATE = 5004;

    /**
     * 玩家背包数据
     */
    short BAG = 5005;

    /**
     * 玩家通用弹窗通知。{@link com.seax.lobby.consts.NoticeType}
     */
    short NOTICE = 5006;

    /**
     * 玩家任务数据
     */
    short ASSIGN = 5007;

    //---------------------- s2c 5001-10000 ------------------------------
    /**
     * s2c最大值
     */
    short S2C_MAX = 10000;

    //---------------------- s2c 5001-10000 ------------------------------
}
