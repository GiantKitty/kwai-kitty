package com.rs;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Test;

import com.rs.service.CatInterface;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-18
 */
public class FunctionalInterfaceTest {

    Consumer<Integer> c = (x) -> System.out.println(x);
    BiConsumer<Integer, String> b = (Integer x, String y) -> System.out.println(x+y);
    Predicate<String> p = (String s) -> s == null;

    // 把函数作为参数传到方法里面o(︶︿︶)o
    public static void execute(CatInterface catInterface) {
        System.out.println(catInterface.add(1, 2));
        catInterface.eat();
    }

    @Test
    public void test() {
        //  方式一：匿名类
        execute(new CatInterface() {
            @Override
            public void eat() {
                System.out.println("匿名类");
            }
        });
        //  方式二：Lambda表达式
        //  由于【有且仅有】,所以java毫不迷糊地知道这里实现的是doSomeWork()
        execute( () -> System.out.println("Lambda表达式") );
    }

}
