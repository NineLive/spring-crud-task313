package ru.spring.crudtask313.springcrudtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.RoleRepository;
import ru.spring.crudtask313.springcrudtask.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdvertisingServiceImp {
    private final EmailService emailService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final WeatherService weatherService;
    private final UserRepository userRepository;

    @Autowired
    public AdvertisingServiceImp(EmailService emailService, UserService userService, RoleRepository roleRepository, WeatherService weatherService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.weatherService = weatherService;
        this.userRepository = userRepository;
    }

    public void makeSpam(List<User> usersList) {
        String from = "sergey.chesnokov9@gmail.com";
        String subject = "Зонты со скидкой! Только сегодня!";
        String text = "Зонты премиум качества со скидкой только сегодня! Переходи по ссылке и забирай за даром! \nhttps://www.tsum.ru/catalog/zonty-18435/";

//        userList.forEach(user -> emailService.send(user.getEmail(), from, subject, text));
        List<User> listAdmins = filteredAdmins();
//        listAdmins.forEach(admin -> emailService.send(admin.getEmail(), from, "count users", "Quantity users: " + userList.size()));

        usersList.forEach(user -> System.out.println("Отправляем письмо: " + user.getName() + " на: "
                + user.getEmail() + " с возрастом: " + user.getAge()));
        listAdmins.forEach(admin -> System.out.println("Отправляем письмо: " + admin.getName() + " на: "
                + admin.getEmail() + " с Quantity: " + usersList.size()));

    }

    public Page<User> getPageUsersFilteredByMinAge(int minAge, int pageNumber, int pageSize) {
        Page<User> users = userService.findByAgeGreaterThanEqual(minAge, PageRequest.of(pageNumber, pageSize));
        return new PageImpl<>(users.get()
                .filter(user -> !user.getRoles().contains(roleRepository.findByRole("ROLE_ADMIN").get()))
                .filter(user -> weatherService.checkRain(user.getAddress()))
                .toList());
    }

    public List<User> filteredAdmins() {
        return userService.findAll().stream()
                .filter(user -> user.getRoles().contains(roleRepository.findByRole("ROLE_ADMIN").get()))
                .toList();
    }
}
