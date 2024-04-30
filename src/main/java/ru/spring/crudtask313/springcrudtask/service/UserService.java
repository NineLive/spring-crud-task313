package ru.spring.crudtask313.springcrudtask.service;

import ru.spring.crudtask313.springcrudtask.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> findAll();

    public Optional<User> findById(long id);

    public boolean save(User user);

    public void update(User updatedUser);

    public void delete(long id);
}
