package com.rs.db;

import java.util.ArrayList;
import java.util.List;

import com.rs.model.User;

import lombok.Data;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-20
 */
@Data
public class UserDB {
    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User("aaa", "123", 19));
        users.add(new User("bbb", "123", 22));
        users.add(new User("ccc", "123", 18));
    }

    public static List<User> getUsers() {
        return users;
    }
}
