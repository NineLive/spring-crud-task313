package ru.spring.crudtask313.springcrudtask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spring.crudtask313.springcrudtask.dto.UserDTO;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.RoleRepository;
import ru.spring.crudtask313.springcrudtask.service.AdvertisingServiceImp;
import ru.spring.crudtask313.springcrudtask.service.EmailService;
import ru.spring.crudtask313.springcrudtask.service.UserService;
import ru.spring.crudtask313.springcrudtask.service.WeatherService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin
@Controller
@RequestMapping("/")
public class HomePageController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final WeatherService weatherService;
    private EmailService emailService;
    private AdvertisingServiceImp advertisingServiceImp;

    @Autowired
    public HomePageController(UserService userService, RoleRepository roleRepository, WeatherService weatherService, EmailService emailService, AdvertisingServiceImp advertisingServiceImp) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.weatherService = weatherService;
        this.emailService = emailService;
        this.advertisingServiceImp = advertisingServiceImp;
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
        User userPrincipal = (User) authentication.getPrincipal();
        User user = userService.findById(userPrincipal.getId()).get();
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .age(user.getAge())
                .email(user.getEmail())
                .address(user.getAddress())
                .addressHasRain(weatherService.checkRain(user.getAddress()))
                .roles(user.getRoles())
                .build();
    }

    @PostMapping()
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        Role role = roleRepository.findByRole("ROLE_USER").get();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);
        if (!userService.save(user)) {
            model.addAttribute("userNameError", "Username already exists");
            return "register";
        }
        return "redirect:/user";
    }

    @ResponseBody
    @GetMapping("/sendmail/{age}")
    public String sendEmail(@PathVariable Integer age){
        int pageNumber = 0;
        int pageSize = 5;
        Page<User> pageUsers = advertisingServiceImp.getPageUsersFilteredByMinAge(age, pageNumber, pageSize);
        while (pageNumber <= pageUsers.getTotalPages()) {
            advertisingServiceImp.makeSpam(pageUsers.toList());
            pageUsers = advertisingServiceImp.getPageUsersFilteredByMinAge(age, ++pageNumber, pageSize);
        }
        return "SPAM SPAM";
    }
}
