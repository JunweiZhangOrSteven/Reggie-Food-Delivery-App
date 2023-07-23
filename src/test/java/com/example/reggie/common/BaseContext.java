package com.example.reggie.common;

/**
 *
 * base on ThreadLocal Encapsulates utility class, save userId and get current user id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * set id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * get id
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }


}
