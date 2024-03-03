package ru.shaikhraziev.bankingservice.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.shaikhraziev.bankingservice.dto.UserEditDto;
import ru.shaikhraziev.bankingservice.entity.Email;
import ru.shaikhraziev.bankingservice.entity.Phone;
import ru.shaikhraziev.bankingservice.entity.User;

import java.util.Optional;

@Slf4j
@Component
public class UserEditMapper implements Mapper<UserEditDto, User> {

    @Override
    public User map(UserEditDto object) {
        return null;
    }

    @Override
    public User map(UserEditDto dto, User user) {
        if (dto.getOldTelephone() != null) {
            Optional<Phone> phone = user.getPhoneNumbers().stream()
                    .filter(p -> p.getTelephone().equals(dto.getOldTelephone()))
                    .findFirst();

            phone.ifPresent(p -> p.setTelephone(dto.getNewTelephone()));

            log.info("Пользователь с login = %s и id = %s сменил телефон: %s на: %s".formatted(user.getLogin(), user.getId(), dto.getOldTelephone(), dto.getNewTelephone()));
        }

        if (dto.getOldEmail() != null) {
            Optional<Email> email = user.getEmailAddresses().stream()
                    .filter(e -> e.getEmail().equals(dto.getOldEmail()))
                    .findFirst();

            email.ifPresent(e -> e.setEmail(dto.getNewEmail()));

            log.info("Пользователь с login = %s и id = %s сменил email: %s на: %s".formatted(user.getLogin(), user.getId(), dto.getOldEmail(), dto.getNewEmail()));
        }

        return user;
    }
}