package ru.spring.crudtask313.springcrudtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.UserRepository;

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
        User user = userRepository.findById(updatedUser.getId()).get();
        user.setName(updatedUser.getName());
        user.setLastname(updatedUser.getLastname());
        user.setAge(updatedUser.getAge());
        user.setEmail(updatedUser.getEmail());
        user.setAddress(updatedUser.getAddress());
        user.setRoles(updatedUser.getRoles());
        if (updatedUser.getPassword() != "" && updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
