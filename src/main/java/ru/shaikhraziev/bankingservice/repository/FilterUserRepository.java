package ru.shaikhraziev.bankingservice.repository;

import ru.shaikhraziev.bankingservice.dto.UserFilter;
import ru.shaikhraziev.bankingservice.entity.User;

import java.util.List;

public interface FilterUserRepository {
    List<User> findAllByFilter(UserFilter filter, int limit);
}