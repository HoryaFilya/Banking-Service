package ru.shaikhraziev.bankingservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.shaikhraziev.bankingservice.entity.User;
import ru.shaikhraziev.bankingservice.exception.IncorrectDataException;
import ru.shaikhraziev.bankingservice.exception.InsufficientFundsException;
import ru.shaikhraziev.bankingservice.repository.UserRepository;
import ru.shaikhraziev.bankingservice.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BankingServiceApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private static final String JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjR9.t4BkSz4Pv1bs1diIYh-rqLdRAkOukX1eOm1_uXSy2fQ";

    @Test
    public void testMoneyTransferSuccessful() {
        User user1 = User.builder()
                .id(4L)
                .login("user1")
                .password("user1")
                .currentBalance(BigDecimal.valueOf(500))
                .maxPossibleDeposit(BigDecimal.valueOf(2000))
                .birthdate(LocalDate.of(2000, 1, 1))
                .firstname("user1")
                .lastname("user1")
                .patronymic("user1")
                .build();

        User user2 = User.builder()
                .id(1L)
                .login("user1")
                .password("user1")
                .currentBalance(BigDecimal.valueOf(500))
                .maxPossibleDeposit(BigDecimal.valueOf(2000))
                .birthdate(LocalDate.of(2000, 1, 1))
                .firstname("user1")
                .lastname("user1")
                .patronymic("user1")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        userService.transfer(user2.getId(), JWT, 50L);

        assertThat(userRepository.findById(user1.getId()).get().getCurrentBalance()).isEqualTo(BigDecimal.valueOf(450));
        assertThat(userRepository.findById(user2.getId()).get().getCurrentBalance()).isEqualTo(BigDecimal.valueOf(550));
    }

    @Test
    public void testTransferToSelfAccount() {
        User user = User.builder()
                .id(4L)
                .login("user1")
                .password("user1")
                .currentBalance(BigDecimal.valueOf(500))
                .maxPossibleDeposit(BigDecimal.valueOf(2000))
                .birthdate(LocalDate.of(2000, 1, 1))
                .firstname("user1")
                .lastname("user1")
                .patronymic("user1")
                .build();

        userRepository.save(user);

        assertThrows(IncorrectDataException.class, () -> userService.transfer(user.getId(), JWT, 100L));
    }

    @Test
    public void testTransferNegativeAmount() {
        User user1 = User.builder()
                .id(4L)
                .login("user1")
                .password("user1")
                .currentBalance(BigDecimal.valueOf(500))
                .maxPossibleDeposit(BigDecimal.valueOf(2000))
                .birthdate(LocalDate.of(2000, 1, 1))
                .firstname("user1")
                .lastname("user1")
                .patronymic("user1")
                .build();

        User user2 = User.builder()
                .id(1L)
                .login("user1")
                .password("user1")
                .currentBalance(BigDecimal.valueOf(500))
                .maxPossibleDeposit(BigDecimal.valueOf(2000))
                .birthdate(LocalDate.of(2000, 1, 1))
                .firstname("user1")
                .lastname("user1")
                .patronymic("user1")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        assertThrows(IncorrectDataException.class, () -> userService.transfer(user2.getId(), JWT, -50L));
    }

    @Test
    public void testInsufficientFunds() {
        User user1 = User.builder()
                .id(4L)
                .login("user1")
                .password("user1")
                .currentBalance(BigDecimal.valueOf(500))
                .maxPossibleDeposit(BigDecimal.valueOf(2000))
                .birthdate(LocalDate.of(2000, 1, 1))
                .firstname("user1")
                .lastname("user1")
                .patronymic("user1")
                .build();

        User user2 = User.builder()
                .id(1L)
                .login("user1")
                .password("user1")
                .currentBalance(BigDecimal.valueOf(500))
                .maxPossibleDeposit(BigDecimal.valueOf(2000))
                .birthdate(LocalDate.of(2000, 1, 1))
                .firstname("user1")
                .lastname("user1")
                .patronymic("user1")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        assertThrows(InsufficientFundsException.class, () -> userService.transfer(user2.getId(), JWT, 500000L));
    }
}