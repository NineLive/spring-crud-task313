package ru.spring.crudtask231.springcrudtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spring.crudtask231.springcrudtask.model.User;
import ru.spring.crudtask231.springcrudtask.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(long id, User updatedUser) {
        userRepository.save(updatedUser);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
