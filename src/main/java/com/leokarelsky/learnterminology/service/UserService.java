package com.leokarelsky.learnterminology.service;

import com.leokarelsky.learnterminology.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User findById(long id);
    User findByUsername(String username);
    User update(User user);
    void delete(long id);
    List<User> getAll();

}
