package ru.shaikhraziev.bankingservice.dto;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserReadDto {

    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private BigDecimal bankAccount;

    private LocalDate birthdate;

    private String firstname;

    private String lastname;

    private String patronymic;
}