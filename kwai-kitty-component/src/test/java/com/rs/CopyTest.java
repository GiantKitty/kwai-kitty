package com.rs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;

import com.rs.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 实现对象克隆有两种方式：
 * 1). 实现Cloneable接口并重写Object类中的clone()方法；
 * 2). 实现Serializable接口，通过对象的序列化和反序列化实现克隆，实现深拷贝
 *
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-14
 */
public class CopyTest {


    @Data
    @AllArgsConstructor
    static class Student implements Cloneable, Serializable {
        private User user;
        private String address;

        @Override
        public Student clone() {
            try {
                return (Student) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        public Student clone2() {
            try {
                Student student = (Student) super.clone();
                student.user = user.clone();
                return student;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    /**
     * 浅拷贝
     */
    @Test
    public void test_ShallowCopy() {
        Student stu_a = new Student(new User("com/rs", 18), "beijing");
        Student stu_b = stu_a.clone();
        Assert.assertNotSame(stu_a, stu_b);
        Assert.assertSame(stu_a.getUser(), stu_b.getUser());
    }

    /**
     * 深拷贝
     */
    @Test
    public void test_DeepCopy() {
        Student stu_a = new Student(new User("com/rs", 18), "beijing");
        Student stu_b = stu_a.clone2();
        Assert.assertNotSame(stu_a, stu_b);
        Assert.assertNotSame(stu_a.getUser(), stu_b.getUser());
    }

    /**
     * 深拷贝
     * 让需要被clone的类，去实现Serializable接口,如果类中存在组合形式的使用,那么每个类都要实现Serializable接口，
     * 跟层次重写clone方法不同的是，不需要既实现cloneable接口，又重写clone方法，这里只需要实现Serializable接口就好了。
     *
     * 不过要注意的是，如果某个属性被transient修饰，那么该属性就无法被拷贝了。
     */
    private static <T> T CloneObj(T obj) {
        T retobj = null;
        try {
            //写入流中
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            //从流中读取
            ObjectInputStream ios = new ObjectInputStream(
                    new ByteArrayInputStream(baos.toByteArray()));
            retobj = (T) ios.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retobj;
    }

    @Test
    public void test_SerializableCopy() {
        Student stu_a = new Student(new User("com/rs", 18), "beijing");
        Student stu_b = CloneObj(stu_a);
        Assert.assertNotSame(stu_a, stu_b);
        Assert.assertNotSame(stu_a.getUser(), stu_b.getUser());
    }


}
