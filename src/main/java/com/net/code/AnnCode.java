package com.net.code;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 方法关联协议号
 * </pre>
 *
 * @author yuxuan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AnnCode {

    /**
     * <pre>
     * 请求协议号
     * </pre>
     */
    short reqCode();

    /**
     * <pre>
     * 返回协议号
     * </pre>
     */
    short resCode() default 0;

    /**
     * <pre>
     * 请求包的类
     * </pre>
     */
    Class<?> reqClazz();

    /**
     * <pre>
     * 返回包的类
     * </pre>
     */
    Class<?> resClazz() default Class.class;


}
