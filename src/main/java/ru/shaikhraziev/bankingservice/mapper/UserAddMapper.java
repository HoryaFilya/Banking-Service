package ru.shaikhraziev.bankingservice.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.shaikhraziev.bankingservice.dto.UserAddDto;
import ru.shaikhraziev.bankingservice.entity.Email;
import ru.shaikhraziev.bankingservice.entity.Phone;
import ru.shaikhraziev.bankingservice.entity.User;

@Slf4j
@Component
public class UserAddMapper implements Mapper<UserAddDto, User> {

    @Override
    public User map(UserAddDto object) {
        return null;
    }

    @Override
    public User map(UserAddDto dto, User user) {
        if (dto.getTelephone() != null) {
            user.getPhoneNumbers().add(Phone.builder()
                    .telephone(dto.getTelephone())
                    .user(user)
                    .build());

            log.info("Пользователь с login = %s и id = %s добавил новый телефон: %s".formatted(user.getLogin(), user.getId(), dto.getTelephone()));
        }

        if (dto.getEmail() != null) {
            user.getEmailAddresses().add(Email.builder()
                    .email(dto.getEmail())
                    .user(user)
                    .build());

            log.info("Пользователь с login = %s и id = %s добавил новый email: %s".formatted(user.getLogin(), user.getId(), dto.getEmail()));
        }

        return user;
    }
}