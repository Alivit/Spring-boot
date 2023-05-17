package ru.clevertec.ecl.spring.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.services.OrderService;

/**
 * Класс контроллер для обработки запросов о заказе
 */
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping
    public ResponseEntity<Page<Order>> getTags(Pageable pageable)
    {
        log.info("Get all orders with page: {}, size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return new ResponseEntity<>(service.getAll(pageable), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        log.info("Find order by id: {}", id);

        return new ResponseEntity<>(service.getById(id), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order,
                             Long idUser,
                             Long idCertificate)
    {
        log.info("Info User - id: {}, Certificate - id: {} ", idUser, idCertificate);

        return new ResponseEntity<>(service.create(order, idUser, idCertificate), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Order> updateOrder(@RequestBody @Valid Order order,
                             @Valid GiftCertificate certificate,
                             @Valid User user){
        log.info("Info certificate - name: {}, price: {}, duration: {} ",
                certificate.getName(), certificate.getPrice(), certificate.getDuration());
        log.info("Info user - email: {}, password: {}",
                user.getEmail(), user.getPassword());

        return ResponseEntity.ok(service.update(order, user, certificate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        log.info("Delete order by id: {}", id);

        return ResponseEntity.ok(service.deleteById(id));
    }
}
