package ru.clevertec.ecl.spring.services;

import org.springframework.data.domain.Page;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;

public interface OrderService {

    public void create(Order order, Long idUser, Long idCertificate);

    public Page<Order> getAll(Integer offset, Integer limit, String sort);

    public void update(Order order, User user, GiftCertificate certificate);

    public Order deleteById(Long id);

    public Order getById(Long id);
}
