package ru.spring.crudtask231.springcrudtask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spring.crudtask231.springcrudtask.exception.UserNotFoundException;
import ru.spring.crudtask231.springcrudtask.model.User;
import ru.spring.crudtask231.springcrudtask.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userNameError", null);
        return "new-user-form";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable long id, Model model) {
        if (userService.findById(id).isPresent()) {
            model.addAttribute("user", userService.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found");
        }
        return "edit-user-form";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new-user-form";
        }
        if (!userService.save(user)) {
            model.addAttribute("userNameError", "Username already exists");
            return "new-user-form";
        }
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-user-form";
        }
        try {
            userService.update(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("userNameError", "Username already exists");
            return "edit-user-form";
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
