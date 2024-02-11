package com.leokarelsky.learnterminology.service.impl;

import com.leokarelsky.learnterminology.exception.NullEntityReferenceException;
import com.leokarelsky.learnterminology.model.User;
import com.leokarelsky.learnterminology.repository.UserRepository;
import com.leokarelsky.learnterminology.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        if (user != null) {
            return userRepository.save(user);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new EntityNotFoundException("User with usermane " + username + " not found"));
    }

    @Override
    public User update(User role) {
        if (role != null) {
            findById(role.getId());
            return userRepository.save(role);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        userRepository.delete(findById(id));
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }
}
