package ru.clevertec.ecl.spring.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.entity.GiftCertificate;

import java.util.Set;

public interface GiftCertificateService {

    public GiftCertificate create(GiftCertificate certificate, Set<String> tags);

    public Page<GiftCertificate> getAll(Pageable pageable);

    public GiftCertificate update(GiftCertificate certificate);

    public GiftCertificate deleteById(Long id);

    public GiftCertificate getById(Long id);

    public GiftCertificate getByName(String name);

}
