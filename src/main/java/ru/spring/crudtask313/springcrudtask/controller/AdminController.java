package ru.spring.crudtask313.springcrudtask.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spring.crudtask313.springcrudtask.dto.UserDTO;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.UserRepository;
import ru.spring.crudtask313.springcrudtask.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@CrossOrigin
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAdminHomePage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "bootstrap-admin";
    }
    @ResponseBody
    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream().map(this::convertUserToUserDTO).toList();
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        User user = convertUserDTOToUser(userDTO);
        if (!userService.save(user)) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.update(convertUserDTOToUser(userDTO));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    private User convertUserDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        Set<Role> setrole = userDTO.getRoles();
        for (Role role : setrole) {
            if (Objects.equals(role.getRole(), "ROLE_ADMIN")) {
                role.setId(1L);
            }
            if (Objects.equals(role.getRole(), "ROLE_USER")) {
                role.setId(2L);
            }
        }
        user.setRoles(setrole);
        return user;
    }

    private UserDTO convertUserToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .age(user.getAge())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
