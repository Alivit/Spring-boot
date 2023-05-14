package ru.clevertec.ecl.spring.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов заказов
     * @see OrderService
     */
    private final OrderService service;

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public Page<Order> getTags(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                               @RequestParam(value = "limit", defaultValue = "10") @Min(1) Integer limit,
                               @RequestParam(value = "sort", defaultValue = "id,ASC")@NotEmpty String sort)
    {
        log.info("Get all orders with offset: {}, limit: {}, sort: {}", offset, limit, sort);

        return service.getAll(offset, limit, sort);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Order getOrderById(@PathVariable Long id){
        log.info("Find order by id: {}", id);

        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody @Valid Order order,
                             Long idUser,
                             Long idCertificate)
    {
        service.create(order, idUser, idCertificate);
        log.info("Info User - id: {}, Certificate - id: {} ", idUser, idCertificate);

        return order;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Order updateOrder(@RequestBody @Valid Order order,
                             @Valid GiftCertificate certificate,
                             @Valid User user){
        service.update(order, user, certificate);
        log.info("Info certificate - name: {}, price: {}, duration: {} ",
                certificate.getName(), certificate.getPrice(), certificate.getDuration());
        log.info("Info user - email: {}, password: {}",
                user.getEmail(), user.getPassword());

        return order;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order deleteOrder(@PathVariable Long id) {
        log.info("Delete order by id: {}", id);

        return service.deleteById(id);
    }
}
