package ru.shaikhraziev.bankingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shaikhraziev.bankingservice.dto.UserCreateEditDto;
import ru.shaikhraziev.bankingservice.service.UserService;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public void registration(UserCreateEditDto dto) {
        userService.registration(dto);
    }
}