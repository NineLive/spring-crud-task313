package ru.spring.crudtask231.springcrudtask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 20, message = "Name length should be between 2-30 characters")
    private String name;
    @Column
    @Min(value = 0, message = "Age should be greater then 0")
    private int age;
    @Column
    @NotEmpty(message = "Name should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    public User(String name, Integer age, String email) {
    }
}
