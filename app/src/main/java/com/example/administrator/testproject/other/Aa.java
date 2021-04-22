package com.example.administrator.testproject.other;


/**
 * Create  by
 * <p>
 * 单例模式的5种创建方法
 *
 * @author User:Administrator
 * @date Data:2019/10/10
 */

public class Aa {
    //私有化构造方法，防止外部之间创建对象
    private Aa() {
    }

/*
    懒汉式
    private static Aa INSTANCE = new Aa();

    public static Aa getInstance(){
        return INSTANCE;
    }
*/

/*
    饿汉式
    private static Aa INSTANCE;

    public static Aa getInstance(){
        if (INSTANCE == null){
            INSTANCE = new Aa();
        }
        return INSTANCE;
    }
*/

/*
    同步锁
    private static Aa INSTANCE;

    public static synchronized Aa getInstance(){
        if (INSTANCE == null){
            INSTANCE = new Aa();
        }
        return INSTANCE;
    }
*/

/*
    双重验证
    private static Aa INSTANCE;

    public static Aa getInstance(){
        if (INSTANCE == null){
            synchronized (Aa.class){
                if (INSTANCE == null){
                    INSTANCE = new Aa();
                }
            }
        }
        return INSTANCE;
    }
*/

/*
    静态内部类
    private static class Holder{
        private static Aa INSTANCE = new Aa();
    }

    public static Aa getInstance(){
        return Holder.INSTANCE;
    }
*/

}
