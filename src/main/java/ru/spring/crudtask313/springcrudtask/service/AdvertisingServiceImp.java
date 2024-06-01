package ru.spring.crudtask313.springcrudtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.RoleRepository;
import ru.spring.crudtask313.springcrudtask.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    public void makeSpamToUsers(List<User> usersList) {
        String from = "sergey.chesnokov9@gmail.com";
        String subject = "Зонты со скидкой! Только сегодня!";
        String text = "Зонты премиум качества со скидкой только сегодня! Переходи по ссылке и забирай за даром! \nhttps://www.tsum.ru/catalog/zonty-18435/";

//        userList.forEach(user -> emailService.send(user.getEmail(), from, subject, text));
        usersList.forEach(user -> System.out.println("Отправляем письмо: " + user.getName() + " на: "
                + user.getEmail() + " с возрастом: " + user.getAge()));
    }

    public void makeSpamToAdmins(List<User> adminsList, int countUsers) {
//        adminsList.forEach(admin -> emailService.send(admin.getEmail(), from, "count users", "Quantity users: " + userList.size()));
        adminsList.forEach(admin -> System.out.println("Отправляем письмо: " + admin.getName() + " на: "
                + admin.getEmail() + " с Quantity: " + countUsers));
    }

    public List<User> getPageUsersFilteredByMinAge(int minAge, int pageNumber, int pageSize) {
        Page<User> users = userService.findByAgeGreaterThanEqual(minAge, PageRequest.of(pageNumber, pageSize));
        return users.get()
                .filter(user -> !user.getRoles().contains(roleRepository.findByRole("ROLE_ADMIN").get()))
                .filter(user -> weatherService.checkRain(user.getAddress()))
                .toList();
    }

    public int getTotalPages(int minAge, int pageNumber, int pageSize){
        Page<User> users = userService.findByAgeGreaterThanEqual(minAge, PageRequest.of(pageNumber, pageSize));
        return users.getTotalPages();
    }

    public List<User> filteredAdmins() {
        return userRepository.findAdmins();
    }


    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void sendEmail() {
        int age = 18;
        int pageNumber = 0;
        int pageSize = 5;
        int countLetters = 0;
        List<User> pageUsers = getPageUsersFilteredByMinAge(age, pageNumber, pageSize);

        while (pageNumber <= getTotalPages(age, pageNumber, pageSize)) {
            makeSpamToUsers(pageUsers);
            countLetters += pageUsers.size();
            pageUsers = getPageUsersFilteredByMinAge(age, ++pageNumber, pageSize);
        }
        makeSpamToAdmins(filteredAdmins(), countLetters);
    }
}
