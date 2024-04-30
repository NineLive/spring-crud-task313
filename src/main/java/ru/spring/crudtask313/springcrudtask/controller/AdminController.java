package ru.spring.crudtask313.springcrudtask.controller;

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
import ru.spring.crudtask313.springcrudtask.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @CrossOrigin
    @ResponseBody
    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        userService.findAll().forEach(user ->
                userDTOs.add(UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .age(user.getAge())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build()));
        return userDTOs;
    }




    @PostMapping()
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        System.out.println(user);
//        if (bindingResult.hasErrors()) {
//            return (ResponseEntity<HttpStatus>) ResponseEntity.badRequest();
//        }
//        if (!userService.save(user)) {
//            return (ResponseEntity<HttpStatus>) ResponseEntity.badRequest();
//        }
//        Set<Role> setrole = user.getRoles();
//        for (Role role : setrole) {
//            System.out.println(role.getRole());
//            if(Objects.equals(role.getRole(), "ROLE_ADMIN")){
//                role.setId(1L);
//            }
//            if(Objects.equals(role.getRole(), "ROLE_USER")){
//                role.setId(2L);
//            }
//        }

        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return (ResponseEntity<HttpStatus>) ResponseEntity.badRequest();
        }
        try {
            userService.update(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("userNameError", "Username already exists");
            return (ResponseEntity<HttpStatus>) ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
