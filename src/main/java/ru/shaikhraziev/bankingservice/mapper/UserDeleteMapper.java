package ru.shaikhraziev.bankingservice.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.shaikhraziev.bankingservice.dto.UserDeleteDto;
import ru.shaikhraziev.bankingservice.dto.UserEditDto;
import ru.shaikhraziev.bankingservice.entity.Email;
import ru.shaikhraziev.bankingservice.entity.Phone;
import ru.shaikhraziev.bankingservice.entity.User;
import ru.shaikhraziev.bankingservice.service.EmailService;
import ru.shaikhraziev.bankingservice.service.PhoneService;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeleteMapper implements Mapper<UserDeleteDto, User> {

    private final PhoneService phoneService;
    private final EmailService emailService;

    @Override
    public User map(UserDeleteDto object) {
        return null;
    }

    @Override
    public User map(UserDeleteDto dto, User user) {
        if (dto.getTelephone() != null) {
            Optional<Phone> phone = user.getPhoneNumbers().stream()
                    .filter(p -> p.getTelephone().equals(dto.getTelephone()))
                    .findFirst();

            phone.ifPresent(p -> user.getPhoneNumbers().remove(p));
            phone.ifPresent(phoneService::delete);

            log.info("Пользователь с login = %s и id = %s удалил телефон: %s".formatted(user.getLogin(), user.getId(), dto.getTelephone()));
        }

        if (dto.getEmail() != null) {
            Optional<Email> email = user.getEmailAddresses().stream()
                    .filter(e -> e.getEmail().equals(dto.getEmail()))
                    .findFirst();

            email.ifPresent(e -> user.getEmailAddresses().remove(e));
            email.ifPresent(emailService::delete);

            log.info("Пользователь с login = %s и id = %s удалил email: %s".formatted(user.getLogin(), user.getId(), dto.getEmail()));
        }

        return user;
    }
}