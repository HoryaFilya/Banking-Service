package ru.shaikhraziev.bankingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaikhraziev.bankingservice.entity.Phone;
import ru.shaikhraziev.bankingservice.entity.User;
import ru.shaikhraziev.bankingservice.repository.PhoneRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;

    @Transactional
    public void delete(Phone phone) {
        phoneRepository.delete(phone);
    }
}