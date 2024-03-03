package ru.shaikhraziev.bankingservice.mapper;

import org.springframework.stereotype.Component;
import ru.shaikhraziev.bankingservice.dto.UserCreateDto;
import ru.shaikhraziev.bankingservice.entity.Email;
import ru.shaikhraziev.bankingservice.entity.Phone;
import ru.shaikhraziev.bankingservice.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@Component
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private static long counter;

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.of(2000, 1, 1);

    private static final String DEFAULT_FIRSTNAME = "Firstname_";
    private static final String DEFAULT_LASTNAME = "Lastname_";
    private static final String DEFAULT_PATRONYMIC = "Patronymic_";

    @Override
    public User map(UserCreateDto object) {
        counter++;

        User user = User.builder()
                .login(object.getLogin())
                .password(object.getPassword())
                .currentBalance(object.getCurrentBalance())
                .maxPossibleDeposit(object.getCurrentBalance().multiply(new BigDecimal(2.07)))
                .birthdate(DEFAULT_BIRTHDATE)
                .firstname(DEFAULT_FIRSTNAME + counter)
                .lastname(DEFAULT_LASTNAME + counter)
                .patronymic(DEFAULT_PATRONYMIC + counter)
                .build();

        user.setPhoneNumbers(Collections.singletonList(Phone.builder()
                .telephone(object.getTelephone())
                .user(user)
                .build()));

        user.setEmailAddresses(Collections.singletonList(Email.builder()
                .email(object.getEmail())
                .user(user)
                .build()));

        return user;
    }
}