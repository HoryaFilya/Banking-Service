package ru.shaikhraziev.bankingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"phoneNumbers", "emailAddresses"})
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @Column(name = "current_balance")
    @PositiveOrZero
    private BigDecimal currentBalance;

    @JsonIgnore
    @Column(name = "max_possible_deposit")
    @PositiveOrZero
    private BigDecimal maxPossibleDeposit;

    @NotNull
    private LocalDate birthdate;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private String patronymic;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotEmpty
    private List<Phone> phoneNumbers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotEmpty
    private List<Email> emailAddresses;
}