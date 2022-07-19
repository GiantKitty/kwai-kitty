package com.rs.service;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-18
 */
@FunctionalInterface
public interface CatInterface {
    /**
     * 抽象方法(有且仅有一个)
     */
    public void eat();

    // java.lang.Object中的public方法
    public boolean equals(Object var1);

    /**
     * 默认方法
     * 如果你想增加一个新的功能的话，只需要在接口中定义一个默认的default 来进行修饰的方法，
     * 并且可以在方法中可以写你自己增加的功能，而且你的子类还不需要去挨个实现该功能接口，这样就完美的解决了。
     */
    public default int add(int a, int b) {
        return a + b;
    }

    public default int sub(int a, int b) {
        return a - b;
    }

    /**
     * 静态方法
     */
    public static void staticMethod_1() {
    }

    public static void staticMethod_2() {
    }
}
