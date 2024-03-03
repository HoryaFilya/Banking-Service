package ru.shaikhraziev.bankingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaikhraziev.bankingservice.dto.*;
import ru.shaikhraziev.bankingservice.entity.User;
import ru.shaikhraziev.bankingservice.exception.*;
import ru.shaikhraziev.bankingservice.mapper.*;
import ru.shaikhraziev.bankingservice.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserAddMapper userAddMapper;
    private final UserReadMapper userReadMapper;
    private final UserEditMapper userEditMapper;
    private final UserDeleteMapper userDeleteMapper;

    @Transactional
    public UserReadDto registration(UserCreateDto dto) {
        UserReadDto userReadDto = Optional.ofNullable(dto)
                .map(userCreateMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();

        log.info("Пользователь с login = %s зарегистрировался".formatted(dto.getLogin()));

        return userReadDto;
    }

    public UserReadDto authentication(UserAuthenticationDto dto) {
        return userRepository.findByLoginAndPassword(dto.getLogin(), dto.getPassword())
                .map(userReadMapper::map)
                .orElseThrow(() -> new InvalidLoginOrPasswordException("Invalid login or password"));
    }

    @Transactional
    public UserReadDto add(UserAddDto userAddDto, Long id) {
        User entity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id %s not found".formatted(id)));

        UserReadDto userReadDto = Optional.ofNullable(userAddDto)
                .map(dto -> userAddMapper.map(dto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map)
                .orElseThrow();

        return userReadDto;
    }

    @Transactional
    public UserReadDto update(UserEditDto userEditDto, Long id) {
        User entity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id %s not found".formatted(id)));

        if ((userEditDto.getOldTelephone() == null && userEditDto.getOldEmail() == null) ||
            userEditDto.getNewTelephone() == null && userEditDto.getNewEmail() == null ||
            userEditDto.getOldTelephone() == null && userEditDto.getNewTelephone() != null ||
            userEditDto.getNewTelephone() == null && userEditDto.getOldTelephone() != null ||
            userEditDto.getOldEmail() == null && userEditDto.getNewEmail() != null ||
            userEditDto.getNewEmail() == null && userEditDto.getOldEmail() != null)
            throw new IncorrectDataException("The data was transmitted incorrectly");

        if (userEditDto.getOldTelephone() != null) {
            entity.getPhoneNumbers().stream()
                    .filter(phone -> phone.getTelephone().equals(userEditDto.getOldTelephone()))
                    .findFirst().orElseThrow(() -> new ResourceNotFoundException("Telephone %s not found".formatted(userEditDto.getOldTelephone())));
        }

        if (userEditDto.getOldEmail() != null) {
            entity.getEmailAddresses().stream()
                    .filter(email -> email.getEmail().equals(userEditDto.getOldEmail()))
                    .findFirst().orElseThrow(() -> new ResourceNotFoundException("Email %s not found".formatted(userEditDto.getOldEmail())));
        }

        return Optional.ofNullable(userEditDto)
                .map(dto -> userEditMapper.map(dto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public UserReadDto delete(UserDeleteDto userDeleteDto, Long id) {
        User entity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id %s not found".formatted(id)));

        if ((userDeleteDto.getTelephone() == null && userDeleteDto.getEmail() == null))
            throw new IncorrectDataException("The data was transmitted incorrectly");

        if (userDeleteDto.getTelephone() != null) {
            entity.getPhoneNumbers().stream()
                    .filter(phone -> phone.getTelephone().equals(userDeleteDto.getTelephone()))
                    .findFirst().orElseThrow(() -> new ResourceNotFoundException("Telephone %s not found".formatted(userDeleteDto.getTelephone())));

            if (entity.getPhoneNumbers().size() == 1)
                throw new DeletingLastElementException("You can't delete the last phone");
        }

        if (userDeleteDto.getEmail() != null) {
            entity.getEmailAddresses().stream()
                    .filter(email -> email.getEmail().equals(userDeleteDto.getEmail()))
                    .findFirst().orElseThrow(() -> new ResourceNotFoundException("Email %s not found".formatted(userDeleteDto.getEmail())));

            if (entity.getEmailAddresses().size() == 1)
                throw new DeletingLastElementException("You can't delete the last email");
        }

        return Optional.ofNullable(userDeleteDto)
                .map(dto -> userDeleteMapper.map(dto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    public List<UserReadDto> findAllByFilter(UserFilter filter, int limit) {
        return userRepository.findAllByFilter(filter, limit).stream()
                .map(userReadMapper::map)
                .toList();
    }

    public UserReadDto findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("User with id %s not found".formatted(id)));
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateBalances() {
        BigDecimal percent = new BigDecimal(1.20);

        userRepository.findAll().forEach(user -> {
            if (user.getCurrentBalance().multiply(percent).compareTo(user.getMaxPossibleDeposit()) <= 0) {
                user.setCurrentBalance(user.getCurrentBalance().multiply(percent));

                log.info("Баланс пользователя с id = %s стал %s".formatted(user.getId(), user.getCurrentBalance()));
            }
            ;
        });
    }

    @Transactional
    public UserReadDto transfer(Long id, String JWT, Long amount) {
        Long idFromUser = Long.valueOf(jwtService.extractPayload(JWT).get("userId").toString());

        User fromUser = userRepository.findById(idFromUser).orElseThrow(() -> new ResourceNotFoundException("User with id %s not found".formatted(id)));
        User toUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id %s not found".formatted(id)));

        BigDecimal balanceFromUser = fromUser.getCurrentBalance();
        BigDecimal balanceToUser = toUser.getCurrentBalance();
        BigDecimal amountTransferred = BigDecimal.valueOf(amount);

        if (idFromUser.equals(id))
            throw new IncorrectDataException("It is forbidden to transfer money to your account");

        if (amount <= 0)
            throw new IncorrectDataException("The amount transferred cannot be less than or equal to zero");

        if (fromUser.getCurrentBalance().subtract(amountTransferred).compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientFundsException("""
                    Insufficient funds. Your balance is %s, and the planned transfer amount is %s.
                    """.formatted(fromUser.getCurrentBalance(), amount));
        }

        fromUser.setCurrentBalance(balanceFromUser.subtract(amountTransferred));
        toUser.setCurrentBalance(balanceToUser.add(amountTransferred));

        log.info("Пользователь с login = %s и id = %s осуществил перевод %s на счет пользователя с login = %s и id = %s"
                .formatted(fromUser.getLogin(), fromUser.getId(), amount, toUser.getLogin(), toUser.getId()));

        return userReadMapper.map(fromUser);
    }
}