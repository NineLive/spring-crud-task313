package ru.spring.crudtask231.springcrudtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.spring.crudtask231.springcrudtask.model.User;
import ru.spring.crudtask231.springcrudtask.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public boolean save(User user) {
        Optional<User> userFromDB = userRepository.findByName(user.getName());
        if (userFromDB.isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void update(User updatedUser) {
        userRepository.save(updatedUser);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
