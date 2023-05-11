package ru.baron.cloudapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.baron.cloudapp.entity.User;

public interface UserDataRepository extends JpaRepository<User, Long> {

    User findByLogin(String username);
}
