package com.rs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
public class ParallelStreamTest {

    private static long times = 500000000;

    private long start;

    @Before
    public void before() {
        start = System.currentTimeMillis();
    }

    @After
    public void after() {
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }

    /**
     * 普通for循环
     */
    @Test
    public void test1(){
        long res = 0;
        for (int i = 0; i < times; i++) {
            res += i;
        }
    }

    /**
     * 串行流处理
     */
    @Test
    public void test2(){
        LongStream.rangeClosed(0, times).reduce(0, Long::sum);
    }

    /**
     * 并行流处理
     */
    @Test
    public void test3(){
        LongStream.rangeClosed(0, times)
                .parallel()
                .reduce(0, Long::sum);
    }

    /**
     * 并行流中的数据安全问题
     */
    @Test
    public void test4() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        System.out.println("size = " + list.size());
        // 使用并行流
        List<Integer> list2 = new ArrayList<>();
        IntStream.rangeClosed(0, 999).parallel().forEach(list2::add);   // 报错
        System.out.println("size = " + list2.size());
    }

    /**
     * 1.加同步锁
     */
    @Test
    public void test5() {
        // 使用并行流
        List<Integer> list2 = new ArrayList<>();
        Object obj = new Object();
        IntStream.rangeClosed(0, 999).parallel().forEach(i -> {
            synchronized (obj) {
                list2.add(i);
            }
        });
        System.out.println("size = " + list2.size());
    }

    /**
     * 2.使用线程安全的容器
     */
    @Test
    public void test6() {
        Vector vector = new Vector<>();
        IntStream.rangeClosed(0, 999).parallel().forEach(vector::add);
        System.out.println("size = " + vector.size());
    }

    /**
     * 3.将线程不安全的容器转换为线程安全的容器
     */
    @Test
    public void test7() {
        List<Integer> list = new ArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(list);
        IntStream.rangeClosed(0, 999).parallel().forEach(synchronizedList::add);
        System.out.println("size = " + synchronizedList.size());
    }

    /**
     * 4.通过Stream中的toArray方法或者collect方法
     */
    @Test
    public void test8() {
        List<Integer> collect = IntStream.rangeClosed(0, 999)
                .parallel()
                .boxed()
                .collect(Collectors.toList());
        System.out.println("size = " + collect.size());
    }
}
