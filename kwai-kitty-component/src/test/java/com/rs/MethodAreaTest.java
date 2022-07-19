package com.rs;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-13
 */
public class MethodAreaTest {

    private static final List<MethodArea> list = new ArrayList<>();

    @Test
    public void test() {
        while (true) {
            list.add(new MethodArea());
        }
    }



    private class MethodArea{}
}
