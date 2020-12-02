package com.net.code.codes;

/**
 * <pre>
 * 战斗协议号
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-01 11:55
 */
public interface FightCodes {

    //---------------------- c2s 10001-13000 ----------------------------------
    /**
     * 进场景
     */
    short ENTER_SCENE = 10001;

    /**
     * 退场景
     */
    short LEAVE_SCENE = 10002;


    /**
     * 移动
     */
    short MOVE = 10003;

    /**
     * 操作同步
     */
    short OPERATE = 10005;

    /**
     * 释放技能
     */
    short SKILL = 10010;

    /**
     * 技能命中
     */
    short SKILL_HIT = 10011;

    /**
     * 陷阱命中
     */
    short TRAP_HIT = 10012;

    /**
     * 拾取场景道具
     */
    short PICK_NODE = 10014;

    /**
     * 心跳包、时间同步、测延时
     */
    short HEART_BEAT = 10015;

    /**
     * 强制打断技能
     */
    short SKILL_BREAK = 10016;


    //--------------------------------------------------------
    /**
     * 测试Buff
     */
    short TEST_BUFF = 12999;

    //---------------------- c2s 10001-13000 ----------------------------------
    /**
     * c2s最大值
     */
    short C2S_MAX = 13000;


    //---------------------- c2s 10001-13000 ----------------------------------
    //---------------------- s2c 13001-15000 ------------------------------

    /**
     * 玩家视野同步
     */
    short SYNC_VIEW = 13001;

    /**
     * 玩家离开视野
     */
    short SYNC_LEAVE_VIEW = 13002;

    /**
     * 移动同步
     */
    short SYNC_MOVE = 13003;

    /**
     * 操作同步
     */
    short SYNC_OPERATE = 13005;

    /**
     * 技能释放同步
     */
    short SYNC_SKILL = 13010;

    /**
     * 技能命中同步
     */
    short SYNC_SKILL_HIT = 13011;

    /**
     * 单位复活
     */
    short SYNC_LIVING_REVIVE = 13012;

    /**
     * 陷阱命中同步
     */
    short SYNC_TRAP_HIT = 13013;

    /**
     * 玩法阶段
     */
    short SYNC_SCENE_STAGE = 13014;

    /**
     * 击杀同步
     */
    short SYNC_KILL = 13015;

    /**
     * 击伤同步(自身)
     */
    short SYNC_HURT = 13016;

    /**
     * 结算面板(大乱斗)
     */
    short SYNC_REWARD = 13017;

    /**
     * 排行榜同步
     */
    short SYNC_RANK = 13018;

    /**
     * 场景节点列表
     */
    short SYNC_NODES = 13019;

    /**
     * 单位属性同步
     */
    short SYNC_LIVING_ATTR = 13021;

    /**
     * 单位Buff同步
     */
    short SYNC_LIVING_BUFF = 13022;

    /**
     * 单位Buff删除
     */
    short SYNC_BUFF_DEL = 13023;

    /**
     * 单位状态同步
     */
    short SYNC_LIVING_STATUS = 13024;

    /**
     * 时间同步(毫秒戳)
     */
    short SYNC_SERVER_TIME = 13025;

    /**
     * 持续Buff-效果同步
     */
    short SYNC_DOT_BUFF = 13026;

    /**
     * 击杀和勋章同步
     */
    short SYNC_KILL_MEDALS = 13027;

    /**
     * 结算面板(团队勋章)
     */
    short SYNC_TEAM_REWARD = 13028;

    /**
     * 吃鸡场景剩余存活人数
     */
    short SYNC_SURVIVAL = 13029;

    /**
     * 吃鸡场景格子数据
     */
    short GRID_DATA = 13030;

    /**
     * 吃鸡场景格子状态同步
     */
    short GRID_STAGE = 13031;

    /**
     * 战斗道具格子同步
     */
    short FITEM_BOX = 13032;

    /**
     * 吃鸡场景结算
     */
    short SYNC_CHICKEN_RESULT = 13033;

    /**
     * 强制打断技能
     */
    short SYNC_SKILL_BREAK = 13034;

    //---------------------- c2s 13001-15000 ------------------------------
    /**
     * s2c最大值
     */
    short S2C_MAX = 15000;

    //---------------------- c2s 13001-15000 ------------------------------
}
