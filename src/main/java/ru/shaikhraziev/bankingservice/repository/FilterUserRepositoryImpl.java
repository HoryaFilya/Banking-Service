package ru.shaikhraziev.bankingservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import ru.shaikhraziev.bankingservice.dto.UserFilter;
import ru.shaikhraziev.bankingservice.entity.User;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilter filter, int limit) {
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);

        var user = criteria.from(User.class);
        criteria.select(user).orderBy(cb.asc(user.get("id")));

        List<Predicate> predicates = new ArrayList<>();

        if (filter.birthDate() != null) {
            predicates.add(cb.greaterThan(user.get("birthdate"), filter.birthDate()));
        }

        if (filter.telephone() != null) {
            predicates.add(cb.equal(user.get("phoneNumbers").get("telephone"), filter.telephone()));
        }

        if (filter.firstname() != null) {
            predicates.add(cb.like(user.get("firstname"), filter.firstname() + "%"));
        }

        if (filter.lastname() != null) {
            predicates.add(cb.like(user.get("lastname"), filter.lastname() + "%"));
        }

        if (filter.patronymic() != null) {
            predicates.add(cb.like(user.get("patronymic"), filter.patronymic() + "%"));
        }

        if (filter.email() != null) {
            predicates.add(cb.equal(user.get("emailAddresses").get("email"), filter.email()));
        }

        criteria.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(criteria).setMaxResults(limit).getResultList();
    }
}