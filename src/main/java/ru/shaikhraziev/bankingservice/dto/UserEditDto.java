package ru.shaikhraziev.bankingservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserEditDto {

    @Min(value = 80000000000L, message = "Формат номера должен быть 8**********")
    @Max(value = 89999999999L, message = "Формат номера должен быть 8**********")
    private Long oldTelephone;

    @Min(value = 80000000000L, message = "Формат номера должен быть 8**********")
    @Max(value = 89999999999L, message = "Формат номера должен быть 8**********")
    private Long newTelephone;

    @Email(message = "Невалидный email")
    private String oldEmail;

    @Email(message = "Невалидный email")
    private String newEmail;
}