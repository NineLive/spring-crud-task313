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
import ru.spring.crudtask313.springcrudtask.mapper.UserMapper;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.service.UserService;

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAdminHomePage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "bootstrap-admin";
    }

    @ResponseBody
    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream().map(UserMapper::convertUserToUserDTO).toList();
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        User user = UserMapper.convertUserDTOToUser(userDTO);
        if (!userService.save(user)) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.update(UserMapper.convertUserDTOToUser(userDTO));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
}
