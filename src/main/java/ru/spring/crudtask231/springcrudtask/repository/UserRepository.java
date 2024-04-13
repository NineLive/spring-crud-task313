package ru.spring.crudtask231.springcrudtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spring.crudtask231.springcrudtask.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
