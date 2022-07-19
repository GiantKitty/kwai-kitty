package com.rs;

import org.junit.Test;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-13
 */
public class MemoryLeakTest {

    Object object;

    @Test
    public void test_() {
        object = new Object();
        // 业务代码 ...
        object = null;  // 之前 new Object() 分配的内存，就可以被GC回收。
    }

}
