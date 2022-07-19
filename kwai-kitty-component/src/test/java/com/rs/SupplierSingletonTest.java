package com.rs;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

/**
 * Suppliers.memoize实现单例
 *
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-19
 */
public class SupplierSingletonTest {

    class HeavyObject {
        public HeavyObject() {
            System.out.println("being created");
        }
    }

    class ObjectSuppiler implements Supplier<HeavyObject> {
        @Override
        public HeavyObject get() {
            return new HeavyObject();
        }
    }

    /**
     * 每次都new一次
     */
    @Test
    public void testNotSingleton() {
        Supplier<HeavyObject> notCached = new ObjectSuppiler();
        for (int i = 0; i < 5; i++) {
            notCached.get();
        }
    }

    /**
     * 单例
     */
    @Test
    public void testSingleton() {
        Supplier<HeavyObject> notCached = new ObjectSuppiler();
        Supplier<HeavyObject> cachedSupplier = Suppliers.memoize(notCached);
        for (int i = 0; i < 5; i++) {
            cachedSupplier.get();
        }
    }

    /**
     * Lazy初始化，Supplier wrapped的对象只在第一次get时候会被初始化
     */
    @Test
    public void should_init_the_supplier_wrapped_object_when_get_object() throws Exception {
        Supplier<Integer> memoize = Suppliers.memoize(() -> {
            System.out.println("init supplier wrapped object");
            return 1;
        });
        System.out.println("main thread block");
        Thread.sleep(2000);
        System.out.println(memoize.get());
    }

    /**
     * Supplier wrapped对象只初始化一次
     */
    @Test
    public void should_init_the_supplier_wrapped_object_for_only_one_time() throws Exception {
        Supplier<Integer> memoize = Suppliers.memoize(() -> {
            System.out.println("init supplier wrapped object");
            return 1;
        });
        System.out.println(memoize.get());
        System.out.println(memoize.get());
    }

    /**
     * 能够使用memoizeWithExpiration函数建立过时设置的Supplier对象，时间过时，get对象会从新初始化对象
     */
    @Test
    public void should_re_init_the_supplier_wrapped_object_when_set_the_expire_time() throws Exception {
        Supplier<Integer> memoize = Suppliers.memoizeWithExpiration(() -> {
            System.out.println("init supplier wrapped object");
            return 1;
        }, 5, TimeUnit.SECONDS);

        System.out.println(memoize.get());
        Thread.sleep(6000);
        System.out.println(memoize.get());
    }

}
