package ru.clevertec.ecl.spring.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;

public interface OrderService {

    public Order create(@Valid Order order, @Positive Long idUser, @Positive Long idCertificate);

    public Page<Order> getAll(Pageable pageable);

    public Order update(@Valid Order order,@Valid User user,@Valid GiftCertificate certificate);

    public Order deleteById(@Positive Long id);

    public Order getById(@Positive Long id);
}
