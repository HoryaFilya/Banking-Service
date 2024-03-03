package ru.shaikhraziev.bankingservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserCreateDto {

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @PositiveOrZero(message = "Сумма не может быть меньше 0")
    private BigDecimal currentBalance;

    @NotNull(message = "Формат номера должен быть: 8**********")
    @Min(value = 80000000000L, message = "Формат номера должен быть 8**********")
    @Max(value = 89999999999L, message = "Формат номера должен быть 8**********")
    private Long telephone;

    @Email(message = "Невалидный email")
    @NotNull(message = "Email не может быть пустым")
    private String email;
}