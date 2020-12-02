package com.net.code;

/**
 * <pre>
 * 协议号过滤
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-12 12:03
 */
public interface ICodeFilters {

    boolean isReqLog(short code);

    boolean isResLog(short code);

}
