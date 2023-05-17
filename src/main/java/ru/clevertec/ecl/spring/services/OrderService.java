package ru.clevertec.ecl.spring.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;

public interface OrderService {

    public Order create(Order order, Long idUser, Long idCertificate);

    public Page<Order> getAll(Pageable pageable);

    public Order update(Order order, User user, GiftCertificate certificate);

    public Order deleteById(Long id);

    public Order getById(Long id);
}
