package ru.clevertec.ecl.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.ecl.spring.entities.GiftCertificate;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class GiftCertificateServiceTest {

    GiftCertificateService giftCertificateService;

    private static Stream<Arguments> certificateArgumentsProvider(){
        return Stream.of(
                Arguments.of(new GiftCertificate(0, "gift1", 120, 45, null, null, null))
        );
    }

    private static Stream<Arguments> certificateArgumentsProviderUpdate(){
        return Stream.of(
                Arguments.of(new GiftCertificate(0, "newGift", 120, 45, null, null, null))
        );
    }

    @BeforeEach
    void init() {
        giftCertificateService = new GiftCertificateService();
    }
    @ParameterizedTest
    @MethodSource("certificateArgumentsProvider")
    public void addTest(GiftCertificate giftCertificate) {
        Set<String> setTags = Set.of("blue", "green", "red");

        giftCertificateService.add(giftCertificate, setTags);

        assertThat(giftCertificateService.getByName("gift1")).isNotNull();
    }

    @Test
    public void getAllTest(){
        List<GiftCertificate> certificateList = giftCertificateService.getAll();

        assertThat(certificateList.size()).isNotEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("certificateArgumentsProvider")
    public void removeTest(GiftCertificate certificate) {
        giftCertificateService.remove(certificate);

        assertThat(giftCertificateService.getByName(certificate.getName())).isNull();
    }

    @ParameterizedTest
    @MethodSource("certificateArgumentsProviderUpdate")
    public void updateTest(GiftCertificate certificate) {
        Set<String> setTags = Set.of("blue", "red");

        giftCertificateService.update(certificate, setTags);

        assertThat(giftCertificateService.getByName("newGift")).isNotNull();
    }
}
