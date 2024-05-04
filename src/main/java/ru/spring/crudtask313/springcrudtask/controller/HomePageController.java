package ru.spring.crudtask313.springcrudtask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spring.crudtask313.springcrudtask.dto.UserDTO;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.RoleRepository;
import ru.spring.crudtask313.springcrudtask.service.UserService;

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
    public String rootPage() {
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @ResponseBody
    @GetMapping("/current")
    public UserDTO test(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .age(user.getAge())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    @PostMapping()
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        Role role = roleRepository.findByRole("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);
        if (!userService.save(user)) {
            model.addAttribute("userNameError", "Username already exists");
            return "register";
        }
        return "redirect:/user";
    }
}
