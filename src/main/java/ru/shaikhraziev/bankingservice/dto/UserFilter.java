package ru.shaikhraziev.bankingservice.dto;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserFilter(LocalDate birthDate,
                         Long telephone,
                         String firstname,
                         String lastname,
                         String patronymic,
                         String email) {
}