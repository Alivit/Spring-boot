package ru.clevertec.ecl.spring.repository;

import ru.clevertec.ecl.spring.entities.GiftCertificate;

import java.util.List;
import java.util.Set;

public interface GiftCertificateRepository {
    int add(GiftCertificate certificate, Set<String> tagsSet);

    List<GiftCertificate> getAll();

    int update(GiftCertificate certificate, Set<String> tagsSet);

    int remove(GiftCertificate certificate);

    GiftCertificate getById(Long id);

    List<GiftCertificate> getByName(String name);
}
