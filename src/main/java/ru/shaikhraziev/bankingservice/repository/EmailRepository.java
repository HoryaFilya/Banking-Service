package ru.shaikhraziev.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shaikhraziev.bankingservice.entity.Email;
import ru.shaikhraziev.bankingservice.entity.User;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}