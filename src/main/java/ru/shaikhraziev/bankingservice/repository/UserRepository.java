package ru.shaikhraziev.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shaikhraziev.bankingservice.dto.UserAuthenticationDto;
import ru.shaikhraziev.bankingservice.dto.UserReadDto;
import ru.shaikhraziev.bankingservice.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository {
    Optional<User> findByLoginAndPassword(String login, String password);
}