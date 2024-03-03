package ru.shaikhraziev.bankingservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shaikhraziev.bankingservice.dto.*;
import ru.shaikhraziev.bankingservice.exception.NotEnoughRightsException;
import ru.shaikhraziev.bankingservice.service.JwtService;
import ru.shaikhraziev.bankingservice.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public UserReadDto registration(@Valid @RequestBody UserCreateDto dto) {
        return userService.registration(dto);
    }

    @PostMapping("/login")
    public UserReadDto authentication(@Valid @RequestBody UserAuthenticationDto dto, HttpServletResponse resp) {
        UserReadDto authorizationUser = userService.authentication(dto);
        resp.addCookie(new Cookie("JWT", jwtService.generateToken(authorizationUser)));

        log.info("Пользователь с login = %s и id = %s аутентифицировался".formatted(authorizationUser.getLogin(), authorizationUser.getId()));

        return authorizationUser;
    }

    @PostMapping("/logout")
    public void logout(@CookieValue String JWT, HttpServletRequest req, HttpServletResponse resp) {
        Optional<Cookie> jwt = Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("JWT"))
                .findFirst();

        jwt.ifPresent(token -> {
            token.setMaxAge(0);
            resp.addCookie(jwt.get());
        });

        Long id = Long.valueOf(jwtService.extractPayload(JWT).get("userId").toString());
        log.info("Пользователь с id = %s покинул приложение".formatted(id));
    }

    @PutMapping("/users/{id}")
    public UserReadDto add(@Valid @RequestBody UserAddDto dto, @PathVariable Long id, @CookieValue String JWT) {
        if (jwtService.rightsVerification(JWT, id))
            return userService.add(dto, id);

        throw new NotEnoughRightsException("Not enough rights");
    }

    @PatchMapping("/users/{id}")
    public UserReadDto update(@Valid @RequestBody UserEditDto dto, @PathVariable Long id, @CookieValue String JWT) {
        if (jwtService.rightsVerification(JWT, id))
            return userService.update(dto, id);

        throw new NotEnoughRightsException("Not enough rights");
    }

    @DeleteMapping("/users/{id}")
    public UserReadDto delete(@Valid @RequestBody UserDeleteDto dto, @PathVariable Long id, @CookieValue String JWT) {
        if (jwtService.rightsVerification(JWT, id))
            return userService.delete(dto, id);

        throw new NotEnoughRightsException("Not enough rights");
    }

    @GetMapping("/users")
    public List<UserReadDto> findAll(@RequestParam(required = false) String birthDate,
                                     @RequestParam(required = false) String telephone,
                                     @RequestParam(required = false) String firstname,
                                     @RequestParam(required = false) String lastname,
                                     @RequestParam(required = false) String patronymic,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false) String limit,
                                     @CookieValue String JWT) {
        if (jwtService.isTokenValid(JWT)) {
            UserFilter filter = UserFilter.builder()
                    .birthDate(birthDate != null ? LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")) : null)
                    .telephone(telephone != null ? Long.valueOf(telephone) : null)
                    .firstname(firstname)
                    .lastname(lastname)
                    .patronymic(patronymic)
                    .email(email)
                    .build();

            Long id = Long.valueOf(jwtService.extractPayload(JWT).get("userId").toString());

            log.info("Пользователь с id = %s запросил список всех пользователей приложения".formatted(id));

            return userService.findAllByFilter(filter, limit != null ? Integer.parseInt(limit) : Integer.MAX_VALUE);
        }

        throw new NotEnoughRightsException("Not enough rights");
    }

    @GetMapping("/users/{id}")
    public UserReadDto findById(@PathVariable Long id, @CookieValue String JWT) {
        if (jwtService.rightsVerification(JWT, id)) {
            log.info("Пользователь с id = %s запросил информацию о себе".formatted(id));
            return userService.findById(id);
        }

        throw new NotEnoughRightsException("Not enough rights");
    }

    @PostMapping("/transfer/{id}")
    public UserReadDto transfer(@PathVariable Long id, @RequestParam Long amount, @CookieValue String JWT) {
        if (jwtService.isTokenValid(JWT))
            return userService.transfer(id, JWT, amount);

        throw new NotEnoughRightsException("Not enough rights");
    }
}