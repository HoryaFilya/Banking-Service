package ru.shaikhraziev.bankingservice.mapper;

import org.springframework.stereotype.Component;
import ru.shaikhraziev.bankingservice.dto.UserReadDto;
import ru.shaikhraziev.bankingservice.entity.User;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {

        return UserReadDto.builder()
                .id(object.getId())
                .login(object.getLogin())
                .password(object.getPassword())
                .currentBalance(object.getCurrentBalance())
                .birthdate(object.getBirthdate())
                .firstname(object.getFirstname())
                .lastname(object.getLastname())
                .patronymic(object.getPatronymic())
                .phoneNumbers(object.getPhoneNumbers())
                .emailAddresses(object.getEmailAddresses())
                .build();
    }
}