package ru.clevertec.ecl.spring.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.spring.entities.GiftCertificate;
import ru.clevertec.ecl.spring.entities.Tag;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TagServiceTest {

    GiftCertificateService giftCertificateService;

    @BeforeEach
    void init(){
        giftCertificateService = new GiftCertificateService();
    }
    @Test
    public void addCertificateTest(){
        Set<String> setTags = Set.of("blue", "red");

        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("gift1");
        certificate.setPrice(100);
        certificate.setDuration(90);

        giftCertificateService.add(certificate, setTags);

        assertThat(giftCertificateService.getAll().size()).isEqualTo(1);

    }
}
