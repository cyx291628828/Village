package com.net.code;

import java.lang.reflect.Method;

/**
 * <pre>
 *
 * </pre>
 *
 * @author yuxuan
 * @time 2020-08-11 10:49
 */
public class MethodAndAnnCode {
    public Object logic;
    public Method method;
    public AnnCode annCode;

    public MethodAndAnnCode(Object logic, Method method, AnnCode annCode) {
        this.logic = logic;
        this.method = method;
        this.annCode = annCode;
    }
}
