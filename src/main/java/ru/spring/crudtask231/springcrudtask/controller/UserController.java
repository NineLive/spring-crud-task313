package ru.spring.crudtask231.springcrudtask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("user", new User());
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
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "new-user-form";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable long id , Model model) {
        if(bindingResult.hasErrors()) {
            return "edit-user-form";
        }
        userService.update(id, user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable long id, Model model) {
        userService.delete(id);
        return "redirect:/users";
    }
}
