package ru.shaikhraziev.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shaikhraziev.bankingservice.dto.UserCreateEditDto;
import ru.shaikhraziev.bankingservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}