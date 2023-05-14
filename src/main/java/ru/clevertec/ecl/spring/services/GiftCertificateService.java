package ru.clevertec.ecl.spring.services;

import org.springframework.data.domain.Page;
import ru.clevertec.ecl.spring.entity.GiftCertificate;

import java.util.Set;

public interface GiftCertificateService {

    public void create(GiftCertificate certificate, Set<String> tags);

    public Page<GiftCertificate> getAll(Integer offset, Integer limit, String sort);

    public void update(GiftCertificate certificate, Set<String> tags);

    public GiftCertificate deleteById(Long id);

    public GiftCertificate getById(Long id);

    public GiftCertificate getByName(String name);
}
