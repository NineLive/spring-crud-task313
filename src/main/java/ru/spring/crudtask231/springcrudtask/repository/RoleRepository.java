package ru.spring.crudtask231.springcrudtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spring.crudtask231.springcrudtask.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
