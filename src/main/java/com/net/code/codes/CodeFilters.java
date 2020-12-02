package com.net.code.codes;

import com.net.code.ICodeFilters;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * 协议号过滤
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-27 20:59
 */
public class CodeFilters implements ICodeFilters {

    public final static ICodeFilters INSTANCE = new CodeFilters();

    private final static Set<Short> REQ_NO_LOG = new HashSet<>();
    private final static Set<Short> RES_NO_LOG = new HashSet<>();

    static {
        REQ_NO_LOG.add(FightCodes.MOVE);
//        REQ_NO_LOG.add(FightCodes.OPERATE);
        REQ_NO_LOG.add(FightCodes.SKILL);
        REQ_NO_LOG.add(FightCodes.SKILL_HIT);
        REQ_NO_LOG.add(FightCodes.HEART_BEAT);
    }

    static {
        RES_NO_LOG.add(FightCodes.SYNC_MOVE);
//        RES_NO_LOG.add(FightCodes.SYNC_OPERATE);
        RES_NO_LOG.add(FightCodes.SYNC_SKILL);
        RES_NO_LOG.add(FightCodes.SYNC_SKILL_HIT);
        RES_NO_LOG.add(MatchCodes.SYNC_MATCH_PROCESS);
        RES_NO_LOG.add(FightCodes.SYNC_RANK);
        RES_NO_LOG.add(FightCodes.SYNC_SERVER_TIME);
    }


    @Override
    public boolean isReqLog(short code) {
        return !REQ_NO_LOG.contains(code);
    }

    @Override
    public boolean isResLog(short code) {
        return !RES_NO_LOG.contains(code);
    }

}
