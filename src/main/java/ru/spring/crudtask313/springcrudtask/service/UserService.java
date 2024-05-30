package ru.spring.crudtask313.springcrudtask.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.spring.crudtask313.springcrudtask.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(long id);

    Page<User> findByAgeGreaterThanEqual(int age, Pageable pageable);

    boolean save(User user);

    void update(User updatedUser);

    void delete(long id);
}
