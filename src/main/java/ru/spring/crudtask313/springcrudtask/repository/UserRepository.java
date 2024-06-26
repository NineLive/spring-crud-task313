package ru.spring.crudtask313.springcrudtask.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.spring.crudtask313.springcrudtask.model.Role;
import ru.spring.crudtask313.springcrudtask.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);

    Page<User> findByAgeGreaterThanEqual(int age, Pageable pageable);

    @Query(value = "SELECT users.age, users.id, users.name, users.email, users.password, users.address, users.lastname " +
            "FROM users " +
            "JOIN  user_roles ON users.id = user_roles.user_id " +
            "JOIN roles ON user_roles.role_id = roles.id " +
            "WHERE roles.role = 'ROLE_ADMIN'", nativeQuery = true)
    List<User> findAdmins();
}
