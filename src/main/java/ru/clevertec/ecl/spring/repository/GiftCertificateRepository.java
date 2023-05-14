package ru.clevertec.ecl.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.spring.entity.GiftCertificate;

import java.util.Set;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    @Query("FROM GiftCertificate WHERE name LIKE % : certificateName %")
    GiftCertificate findByName(String certificateName);
}
