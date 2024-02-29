package ru.shaikhraziev.bankingservice.dto;

import jakarta.persistence.Column;
import java.math.BigDecimal;

public class UserCreateEditDto {

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private BigDecimal bankAccount;

    @Column(nullable = false, unique = true)
    private Long telephone;

    @Column(nullable = false, unique = true)
    private String email;
}