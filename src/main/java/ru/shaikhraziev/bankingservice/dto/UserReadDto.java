package ru.shaikhraziev.bankingservice.dto;

import lombok.Builder;
import lombok.Getter;
import ru.shaikhraziev.bankingservice.entity.Email;
import ru.shaikhraziev.bankingservice.entity.Phone;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class UserReadDto {

    private Long id;
    private String login;
    private String password;
    private BigDecimal currentBalance;
    private LocalDate birthdate;
    private String firstname;
    private String lastname;
    private String patronymic;
    private List<Phone> phoneNumbers;
    private List<Email> emailAddresses;
}