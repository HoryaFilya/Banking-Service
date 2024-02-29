package ru.shaikhraziev.bankingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shaikhraziev.bankingservice.dto.UserCreateEditDto;
import ru.shaikhraziev.bankingservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registration(UserCreateEditDto dto) {
//        userRepository.save(dto);
    }
}