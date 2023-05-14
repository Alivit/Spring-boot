package ru.clevertec.ecl.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;
import ru.clevertec.ecl.spring.services.GiftCertificateService;
import ru.clevertec.ecl.spring.services.impl.GiftCertificateServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    GiftCertificateService service;


    private static Stream<Arguments> certificateArgumentsProvider(){
        return Stream.of(
                Arguments.of(new GiftCertificate(3, "gift1", 120, 45, null, null, null))
        );
    }

    private static Stream<Arguments> certificateArgumentsProviderUpdate(){
        return Stream.of(
                Arguments.of(new GiftCertificate(0, "newGift", 120, 45, null, null, null))
        );
    }

    @BeforeEach
    void init() {
        service = new GiftCertificateServiceImpl(giftCertificateRepository);
    }
    @ParameterizedTest
    @MethodSource("certificateArgumentsProvider")
    public void addTest(GiftCertificate giftCertificate) {
        Set<String> setTags = Set.of("blue", "green", "red");

        service.create(giftCertificate, setTags);

        assertThat(service.getByName("gift1")).isNotNull();
    }

    @Test
    public void getAllTest(){
        Page<GiftCertificate> certificateList = service.getAll(5, 10,"id:DESC");

        assertThat(certificateList.getTotalPages()).isNotEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("certificateArgumentsProvider")
    public void removeTest(GiftCertificate certificate) {
        service.deleteById(certificate.getId());

        assertThat(service.getByName(certificate.getName())).isNull();
    }

    @ParameterizedTest
    @MethodSource("certificateArgumentsProviderUpdate")
    public void updateTest(GiftCertificate certificate) {
        Set<String> setTags = Set.of("blue", "red");

        service.update(certificate, setTags);

        assertThat(service.getByName("newGift")).isNotNull();
    }
}
