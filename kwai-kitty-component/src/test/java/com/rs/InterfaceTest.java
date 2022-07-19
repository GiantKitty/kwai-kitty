package com.rs;

import org.junit.Test;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-18
 */
public class InterfaceTest {

    public interface A {
        default void hello() {System.out.println("hello from A");}
    }
    public interface B extends A {
        default void hello() {System.out.println("hello from B");}
    }

    public class C implements A, B {}

    public class D implements A {}

    public class E extends D implements A, B {}

    public class F implements A {
        public void hello() {System.out.println("hello from F");}
    }

    public class G extends F implements A, B {}

    /**
     * 为解决这种多继承关系，Java8提供了下面三条规则：
     * 类中的方法优先级最高：类或父类中声明的方法的优先级高于任何声明为默认方法的优先级。
     * 如果第一条无法判断，那么子接口的优先级更高：方法签名相同时，优先选择拥有最具体实现的默认方法的接口， 即如果B继承了A，那么B就比A更加具体。
     * 最后，如果还是无法判断，继承了多个接口的类必须通过显式覆盖和调用期望的方法， 显式地选择使用哪一个默认方法的实现。
     */
    @Test
    public void test() {
        new C().hello();  // hello from B
        // C虽然继承了D，但D中未覆盖A的默认方法。接着，编译器会在A和B中做选择，由于B更具体。
        new E().hello();  // hello from B
        // 父类中声明的方法具有更高的优先级
        new G().hello();  // hello from F
    }
}
