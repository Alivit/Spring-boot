package ru.clevertec.ecl.spring.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.spring.entity.User;

import ru.clevertec.ecl.spring.services.UserService;

/**
 * Класс контроллер для обработки запросов о пользователях
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов пользователей
     * @see UserService
     */
    private final UserService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public User getUserById(@PathVariable Long id){
        log.info("Find user by id: {}", id);

        return service.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public Page<User> getUsers(
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) Integer limit,
            @RequestParam(value = "sort", defaultValue = "id,ASC")@NotEmpty String sort)
    {
        log.info("Get all users with offset: {}, limit: {}, sort: {}", offset, limit, sort);

        return service.getAll(offset, limit, sort);
    }
}
