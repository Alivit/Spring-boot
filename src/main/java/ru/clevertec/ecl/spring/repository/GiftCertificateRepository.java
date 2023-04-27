package ru.clevertec.ecl.spring.repository;

import ru.clevertec.ecl.spring.entities.GiftCertificate;

import java.util.List;
import java.util.Set;

public interface GiftCertificateRepository {
    void add(GiftCertificate certificate, Set<String> tagsSet);

    List<GiftCertificate> getAll();

    void update(GiftCertificate certificate, Set<String> tagsSet);

    void remove(GiftCertificate certificate);

    GiftCertificate getById(Long id);
}
