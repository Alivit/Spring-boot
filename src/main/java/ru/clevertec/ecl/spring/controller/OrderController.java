package ru.clevertec.ecl.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Page<Order>> getOrders(Pageable pageable) {
        log.info("Request: get all orders with Pageable: {}", pageable.toString());

        Page<Order> response = service.getAll(pageable);
        log.info("Response: received number of orders - {}", response.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        log.info("Request: find order by id: {}", id);

        Order response = service.getById(id);
        log.info("Response: found order: {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(Order order, Long idUser, Long idCertificate)
    {
        log.info("Request: info order - idUser: {}, idCertificate: {} ",
                idUser, idCertificate);

        Order response = service.create(order, idUser, idCertificate);
        log.info("Response: created order: {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Order> updateOrder(Order order, GiftCertificate certificate, User user) {
        log.info("Request: info certificate - {}", certificate.toString());
        log.info("Request: info user - {}", user.toString());

        Order response = service.update(order, user, certificate);
        log.info("Response: updated order: {}", response.toString());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        log.info("Request: delete order by id: {}", id);

        Order response = service.deleteById(id);
        log.info("Response: deleted order: {}", response.toString());

        return ResponseEntity.ok(response);
    }
}
