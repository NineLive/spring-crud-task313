package ru.spring.crudtask313.springcrudtask.mapper;

import ru.spring.crudtask313.springcrudtask.dto.UserDTO;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;

import java.util.Objects;
import java.util.Set;

public class UserMapper {
    public static User convertUserDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setPassword(userDTO.getPassword());
        Set<Role> setrole = userDTO.getRoles();
        for (Role role : setrole) {
            if (Objects.equals(role.getRole(), "ROLE_ADMIN")) {
                role.setId(1L);
            }
            if (Objects.equals(role.getRole(), "ROLE_USER")) {
                role.setId(2L);
            }
        }
        user.setRoles(setrole);
        return user;
    }

    public static UserDTO convertUserToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .age(user.getAge())
                .email(user.getEmail())
                .address(user.getAddress())
                .roles(user.getRoles())
                .build();
    }
}
