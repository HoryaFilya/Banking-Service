package ru.shaikhraziev.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shaikhraziev.bankingservice.entity.Phone;
import ru.shaikhraziev.bankingservice.entity.User;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
}