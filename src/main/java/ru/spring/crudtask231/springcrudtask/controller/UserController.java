package ru.spring.crudtask231.springcrudtask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.spring.crudtask231.springcrudtask.exception.UserNotFoundException;
import ru.spring.crudtask231.springcrudtask.model.User;
import ru.spring.crudtask231.springcrudtask.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers( Model model) {
        model.addAttribute("users", userService.findAll());
        return "user";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        return "new-user-form";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable long id, Model model) {
        if(userService.findById(id).isPresent()) {
            model.addAttribute("user", userService.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found");
        }
        return "edit-user-form";
    }

    @PostMapping()
    public String createUser(@ModelAttribute User user, Model model) {
        System.out.println("POST method");
        userService.save(user);
        return "redirect:/users";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute User user, @PathVariable long id ,Model model) {
        System.out.println("PAATCH method");
        userService.update(id, user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable long id, Model model) {
        System.out.println("delete method");
        userService.delete(id);
        return "redirect:/users";
    }
}
