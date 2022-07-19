package com.rs;

import javax.xml.ws.Holder;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.StringHolder;

import com.rs.model.User;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-13
 */
public class ParamTest {

    private static User user = null;
    private static User stu = null;

    public static void swap(User x, User y) {  //交换两个对象
        User temp = x;
        x = y;
        y = temp;
        x.setAge(99);
        y.setAge(88);
    }

    @Test
    public void test() {
        user = new User("user", 26);
        stu = new User("stu", 18);
        swap(user, stu);
        Assert.assertEquals(88, user.getAge());
        Assert.assertEquals("user", user.getUsername());
        Assert.assertEquals(99, stu.getAge());
        Assert.assertEquals("stu", stu.getUsername());
    }


    /**
     * 不通过返回值在一个方法中改变一个对象
     */
    public static void testHolder(Holder<User> holder){
        User userInfo = new User();
        userInfo.setAge(holder.value.getAge());
        userInfo.setUsername(holder.value.getUsername());
        holder.value = userInfo;
    }

    public static void changeString(String str){
        str = "changes";
    }
    public static void changeStringHolder(StringHolder stringHolder){
        stringHolder.value = "testnew String";
    }

    @Test
    public void test_() {
        User userInfo = new User("浙江",18);
        Holder<User> holder = new Holder<>(userInfo);
        testHolder(holder);  //name改变了

        // 这种方法是不会输出修改后的值的
        String str = "xisole@@@@";
        changeString(str);

        StringHolder stringHolder = new StringHolder("test");
        changeStringHolder(stringHolder);
        System.out.println(stringHolder.value); // testnew String
    }

}
