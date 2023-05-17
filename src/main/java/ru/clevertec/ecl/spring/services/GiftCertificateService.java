package ru.clevertec.ecl.spring.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.spring.entity.GiftCertificate;

import java.util.Set;

@Validated
public interface GiftCertificateService {

    public GiftCertificate create(@Valid GiftCertificate certificate, Set<String> tags);

    public Page<GiftCertificate> getAll(Pageable pageable);

    public GiftCertificate update(@Valid GiftCertificate certificate);

    public GiftCertificate deleteById(@Positive Long id);

    public GiftCertificate getById(@Positive Long id);

    public GiftCertificate getByName(@NotBlank String name);

}
