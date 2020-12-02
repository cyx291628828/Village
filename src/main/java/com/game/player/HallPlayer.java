package com.game.player;


/**
 * <pre>
 * 大厅玩家
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-03 17:03
 */
public class HallPlayer {

    // 卸载时长
    private final static long PLAYER_UNLOAD_MILLS = 1000 * 60 * 60;

    protected long userId;

    public HallPlayer(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return " HallPlayer{" +
                "userId=" + userId +
                '}';
    }

}
