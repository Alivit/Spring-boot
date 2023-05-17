package ru.clevertec.ecl.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.spring.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
