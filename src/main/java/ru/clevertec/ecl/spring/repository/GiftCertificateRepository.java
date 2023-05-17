package ru.clevertec.ecl.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.spring.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    @Query(value = "FROM GiftCertificate WHERE name LIKE % ?1 %", nativeQuery = true)
    GiftCertificate findByName(String certificateName);
}
