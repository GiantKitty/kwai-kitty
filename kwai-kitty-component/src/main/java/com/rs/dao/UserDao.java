package com.rs.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rs.db.UserDB;
import com.rs.model.User;

import lombok.Data;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-20
 */
@Component
@Data
public class UserDao {

    public User find(String username, String password) {

        List<User> userList = UserDB.getUsers();

        //遍历List集合，看看有没有对应的username和password
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
