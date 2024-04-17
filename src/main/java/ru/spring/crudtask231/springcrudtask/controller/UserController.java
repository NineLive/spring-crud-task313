package ru.spring.crudtask231.springcrudtask.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.spring.crudtask231.springcrudtask.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping()
    public String userInfo(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user-home-page";
    }
}
