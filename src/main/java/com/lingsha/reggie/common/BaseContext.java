package com.lingsha.reggie.common;

/**
 * 基于TheadLocal封装工具类,用于访问session 中的id
 * 一次请求一个线程，不会混淆
 * @auther Ling.Sha
 * @date 2022/8/6 - 22:10
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id)
    {
        threadLocal.set(id);
    }
    public static Long getCurrentId()
    {
        return threadLocal.get();
    }
}
