package ru.spring.crudtask313.springcrudtask.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spring.crudtask313.springcrudtask.model.Role;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    private String lastname;
    private int age;
    private String email;
    private String address;
//    private String weather;
    private String password;
    private Set<Role> roles;
}
