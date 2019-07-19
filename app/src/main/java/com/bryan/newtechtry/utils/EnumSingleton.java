package com.bryan.newtechtry.utils;

/**
 * 单例的最佳实现
 * 可以避免反射和序列化出现多个实例
 */
public enum EnumSingleton {

    SINGLETON;

    public void init(){
        System.out.println("init");
    }

}
