package ru.shaikhraziev.bankingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(name = "bank_account", nullable = false)
    private BigDecimal bankAccount;

    private LocalDate birthdate;

    private String firstname;

    private String lastname;

    private String patronymic;

    @OneToMany(mappedBy = "user")
    private List<Phone> phoneNumbers;

    @OneToMany(mappedBy = "user")
    private List<Email> emailAddresses;
}