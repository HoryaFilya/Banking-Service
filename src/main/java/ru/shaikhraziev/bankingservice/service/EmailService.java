package ru.shaikhraziev.bankingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaikhraziev.bankingservice.entity.Email;
import ru.shaikhraziev.bankingservice.entity.User;
import ru.shaikhraziev.bankingservice.repository.EmailRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;


    @Transactional
    public void delete(Email email) {
        emailRepository.delete(email);
    }
}