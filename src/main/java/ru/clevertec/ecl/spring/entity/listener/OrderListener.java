package ru.clevertec.ecl.spring.entity.listener;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.ecl.spring.entity.Order;

import java.time.LocalDateTime;

@Slf4j
public class OrderListener {

    private LocalDateTime dateTime;

    @PrePersist
    private void createDate(Order order){
        dateTime = LocalDateTime.now();
        order.setDate_purchase(dateTime);
        log.info("Set date purchase: {}", dateTime);
        order.setLast_update_order(dateTime);
        log.info("Set last update order: {}", dateTime);
    }

    @PreUpdate
    private void updateDate(Order order){
        dateTime = LocalDateTime.now();
        order.setLast_update_order(dateTime);
        log.info("Set last update order: {}", dateTime);
    }
}
