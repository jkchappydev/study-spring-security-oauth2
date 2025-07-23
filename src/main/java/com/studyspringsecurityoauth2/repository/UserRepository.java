package com.studyspringsecurityoauth2.repository;

import com.studyspringsecurityoauth2.model.users.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

// 4
@Repository
public class UserRepository {

    private Map<String, Object> users = new HashMap<>();

    public User findByUsername(String username) {
        if(users.containsKey("username")) {
            return (User) users.get(username);
        }

        return null;
    }

    public void register(User user) {
        if(users.containsKey(user.getUsername())) {
            return;
        }

        users.put(user.getUsername(), user);
    }

}
