package ru.clevertec.ecl.spring.services.impl;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.repository.UserRepository;
import ru.clevertec.ecl.spring.services.UserService;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService {

    private final UserRepository repository;

    /**
     * Метод который находит данные из таблицы users по id
     *
     * @param id содержит id пользователя
     *
     * @return пользователя со всей информацией
     */
    @Override
    public User getById(@Positive Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id - " + id + " not found"));
        log.info("User with id " + id + ": {}", user);

        return user;
    }

    /**
     * Метод который получает данные из таблицы users в виде страницы
     * offset - начало страницы, limit - конец страницы
     * и сортирующий страницу по имени поля и по методу ASC/DESC
     *
     * @param pageable содержит информацию о страницы
     *
     * @return страницу пользователей со всей информацией
     */
    @Override
    public Page<User> getAll(Pageable pageable) {
        Page<User> users = repository.findAll(pageable);
        if(users.isEmpty()) throw new NotFoundException("Users not found");
        log.info("Users : {}", users);

        return users;
    }

}
