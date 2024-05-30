package ru.spring.crudtask313.springcrudtask.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.RoleRepository;
import ru.spring.crudtask313.springcrudtask.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }


    public Page<User> findByAgeGreaterThanEqual(int age, Pageable pageable) {
        return userRepository.findByAgeGreaterThanEqual(age, pageable);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.findByName(username).isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return userRepository.findByName(username).get();
    }

    @EventListener(ApplicationReadyEvent.class)
    private void setDefaultAdmin() {
        Set<Role> setrole = new HashSet<>();
        if (roleRepository.findByRole("ROLE_ADMIN").isEmpty()) {
            Role roleAdmin = Role.builder().role("ROLE_ADMIN").build();
            roleRepository.save(roleAdmin);
        }
        setrole.add(roleRepository.findByRole("ROLE_ADMIN").get());
        if (roleRepository.findByRole("ROLE_USER").isEmpty()) {
            Role roleUser = Role.builder().role("ROLE_USER").build();
            roleRepository.save(roleUser);
        }
        setrole.add(roleRepository.findByRole("ROLE_USER").get());
        if (userRepository.findByName("admin").isEmpty()) {
            User admin = User.builder()
                    .name("admin")
                    .lastname("admin")
                    .age(0)
                    .email("admin@admin.com")
                    .address("Москва")
                    .password(passwordEncoder.encode("admin"))
                    .roles(setrole)
                    .build();
            userRepository.save(admin);
        }
    }
}
