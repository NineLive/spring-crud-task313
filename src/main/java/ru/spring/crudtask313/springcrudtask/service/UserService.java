package ru.spring.crudtask313.springcrudtask.service;

import ru.spring.crudtask313.springcrudtask.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(long id);

    boolean save(User user);

    void update(User updatedUser);

    void delete(long id);
}
