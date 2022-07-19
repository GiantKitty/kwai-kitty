package com.rs.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Cloneable, Serializable {
    private String username;
    private String password;
    private int age;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
