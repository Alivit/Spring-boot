package ru.clevertec.ecl.spring.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.spring.entity.Tag;
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

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        log.info("Request: find user by id: {}", id);

        User response = service.getById(id);
        log.info("Response: found user: {}", response.toString());

        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        log.info("Request: get all users with Pageable: {}", pageable.toString());

        Page<User> response = service.getAll(pageable);
        log.info("Response: received number of users - {}", response.getTotalPages());

        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }
}
