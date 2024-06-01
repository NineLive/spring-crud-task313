package ru.spring.crudtask313.springcrudtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.spring.crudtask313.springcrudtask.model.User;
import ru.spring.crudtask313.springcrudtask.repository.RoleRepository;
import ru.spring.crudtask313.springcrudtask.repository.UserRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AdvertisingServiceImp {
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final WeatherService weatherService;
    private final UserRepository userRepository;

    @Autowired
    public AdvertisingServiceImp(EmailService emailService, RoleRepository roleRepository,
                                 WeatherService weatherService, UserRepository userRepository) {
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.weatherService = weatherService;
        this.userRepository = userRepository;
    }

    public void sendEmailToUsers(List<User> usersList) {
        String from = "sergey.chesnokov9@gmail.com";
        String subject = "Зонты со скидкой! Только сегодня!";
        String text = "Зонты премиум качества со скидкой только сегодня! Переходи по ссылке и забирай за даром! \n" +
                "https://www.tsum.ru/catalog/zonty-18435/";

        usersList.forEach(user -> emailService.send(user.getEmail(), from, subject, text));
    }

    public void sendEmailToAdmins(List<User> adminsList, int countUsers) {
        adminsList.forEach(admin -> emailService.send(admin.getEmail(), "sergey.chesnokov9@gmail.com",
                "count users", "Quantity of users who received email: " + countUsers));
    }

    private List<User> getPageUsersFilteredByMinAge(int minAge, int pageNumber, int pageSize) {
        Page<User> users = userRepository.findByAgeGreaterThanEqual(minAge, PageRequest.of(pageNumber, pageSize));
        return users.get()
                .filter(user -> !user.getRoles().contains(roleRepository.findByRole("ROLE_ADMIN").get()))
                .filter(user -> weatherService.checkRain(user.getAddress()))
                .toList();
    }

    private int getTotalPages(int minAge, int pageNumber, int pageSize) {
        Page<User> users = userRepository.findByAgeGreaterThanEqual(minAge, PageRequest.of(pageNumber, pageSize));
        return users.getTotalPages();
    }

    private List<User> filteredAdmins() {
        return userRepository.findAdmins();
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void sendEmail() {
        int minAge = 18;
        int pageNumber = 0;
        int pageSize = 5;
        int countLetters = 0;
        List<User> pageUsers = getPageUsersFilteredByMinAge(minAge, pageNumber, pageSize);

        while (pageNumber <= getTotalPages(minAge, pageNumber, pageSize)) {
            sendEmailToUsers(pageUsers);
            countLetters += pageUsers.size();
            pageUsers = getPageUsersFilteredByMinAge(minAge, ++pageNumber, pageSize);
        }
        sendEmailToAdmins(filteredAdmins(), countLetters);
    }
}
