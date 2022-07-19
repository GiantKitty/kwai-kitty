package com.rs;

import java.util.WeakHashMap;

import org.junit.Test;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-13
 */
public class WeakHashMapTest {

    class Key {
        String id;

        public Key(String id) {
            this.id = id;
        }

        public String toString() {
            return id;
        }

        public int hashCode() {
            return id.hashCode();
        }

        public boolean equals(Object r) {
            return (r instanceof Key) && id.equals(((Key) r).id);
        }

        public void finalize() {
            System.out.println("Finalizing Key " + id);
        }
    }

    class Value {
        String id;

        public Value(String id) {
            this.id = id;
        }

        public String toString() {
            return id;
        }

        public void finalize() {
            System.out.println("Finalizing Value" + id);
        }
    }

    /**
     * 当执行System.gc()方法后，垃圾回收器只会回收那些仅仅持有弱引用的Key对象。id可以被3整处的Key对象持有强引用，因此不会被回收。
     */
    @Test
    public void test() throws Exception {
        int size = 10;
        Key[] keys = new Key[size];  //存放键对象的强引用
        WeakHashMap<Key, Value> whm = new WeakHashMap<>();

        for (int i = 1; i <= size; i++) {
            Key k = new Key(Integer.toString(i));
            Value v = new Value(Integer.toString(i));
            if (i % 3 == 0) keys[i] = k; //使Key对象k持有强引用
            whm.put(k,v); //使Key对象持有弱引用
        }

        //催促垃圾回收器工作
        System.gc();

        //把CPU让给垃圾回收器线程
        Thread.sleep(2000);
    }

}
