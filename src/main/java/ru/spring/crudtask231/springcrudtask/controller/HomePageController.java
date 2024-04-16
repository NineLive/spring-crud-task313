package ru.spring.crudtask231.springcrudtask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.spring.crudtask231.springcrudtask.model.Role;
import ru.spring.crudtask231.springcrudtask.model.User;
import ru.spring.crudtask231.springcrudtask.repository.RoleRepository;
import ru.spring.crudtask231.springcrudtask.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class HomePageController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public HomePageController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping()
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new-user-form";
        }
        Role role = roleRepository.findByRole("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);
        userService.save(user);
        return "redirect:/user";
    }
}
