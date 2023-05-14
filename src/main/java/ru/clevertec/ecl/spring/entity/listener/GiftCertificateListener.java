package ru.clevertec.ecl.spring.entity.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.ecl.spring.entity.GiftCertificate;

import java.time.LocalDateTime;

@Slf4j
public class GiftCertificateListener {

    private LocalDateTime dateTime;

    @PrePersist
    private void createDate(GiftCertificate certificate){
        dateTime = LocalDateTime.now();
        certificate.setCreate_date(dateTime);
        log.info("Set create date: {}", dateTime);
        certificate.setLast_update_data(dateTime);
        log.info("Set last update data: {}", dateTime);
    }

    @PreUpdate
    private void updateDate(GiftCertificate certificate){
        dateTime = LocalDateTime.now();
        certificate.setLast_update_data(dateTime);
        log.info("Set last update data: {}", dateTime);
    }
}
