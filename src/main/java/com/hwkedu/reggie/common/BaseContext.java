package com.hwkedu.reggie.common;

/**
 * @author 侯文柯
 * @version 1.0
 * 基于ThreadLocal封装工具类，用于保护和获取当前用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrent(){
        return threadLocal.get();
    }
}
