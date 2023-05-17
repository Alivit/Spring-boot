package ru.clevertec.ecl.spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.spring.builders.impls.GiftCertificateTestBuilder;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.services.GiftCertificateService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class GiftCertificateControllerTest {

    @MockBean
    GiftCertificateService service;
    private final WebTestClient webTestClient;
    private final ObjectMapper mapper;
    private static final GiftCertificateTestBuilder CERTIFICATE_TEST_BUILDER = GiftCertificateTestBuilder.aCertificate();

    @Nested
    public class FindByIdTest {

        @Test
        public void findByIdShouldReturnStatus200() {
            doReturn(CERTIFICATE_TEST_BUILDER)
                    .when(service)
                    .getById(CERTIFICATE_TEST_BUILDER.build().getId());

            webTestClient.get()
                    .uri("/certificates" + CERTIFICATE_TEST_BUILDER.build().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GiftCertificate.class)
                    .isEqualTo(CERTIFICATE_TEST_BUILDER.build());
        }

    }

    @Nested
    public class FindAllTest {

        @Test
        public void findAllShouldReturnStatus200() throws JsonProcessingException {
            String jsonArray = mapper.writeValueAsString(List.of(CERTIFICATE_TEST_BUILDER.build()));

            doReturn(List.of(CERTIFICATE_TEST_BUILDER.build()))
                    .when(service)
                    .getAll(any(Pageable.class));

            webTestClient.get()
                    .uri("/certificates?page=0&size=10&sort=id,asc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json(jsonArray);
        }

        @Test
        public void findAllShouldReturnEmptyJson() {

            doReturn(List.of())
                    .when(service)
                    .getAll(any(Pageable.class));

            webTestClient.get()
                    .uri("/certificates?page=0&size=10&sort=id,asc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

    }

    @Nested
    public class CreateCertificateTest {

        @Test
        public void createCertificateShouldReturnStatus201() {
            Set<String> tags = Set.of("Green", "Blue");

            doReturn(CERTIFICATE_TEST_BUILDER.build())
                    .when(service)
                    .create(CERTIFICATE_TEST_BUILDER.build(), tags);

            webTestClient.post()
                    .uri("/certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(CERTIFICATE_TEST_BUILDER.build())
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(GiftCertificate.class)
                    .isEqualTo(CERTIFICATE_TEST_BUILDER.build());
        }

    }

    @Nested
    public
    class UpdateCertificateTest {

        @Test
        public void updateCertificateShouldReturnStatus200() {

            doReturn(CERTIFICATE_TEST_BUILDER.build())
                    .when(service)
                    .update(CERTIFICATE_TEST_BUILDER.build());

            webTestClient.put()
                    .uri("/certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(CERTIFICATE_TEST_BUILDER.build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GiftCertificate.class)
                    .isEqualTo(CERTIFICATE_TEST_BUILDER.build());
        }

    }

    @Nested
    public class DeleteCertificateTest {

        @Test
        public void deleteCertificateShouldReturnStatus200() {

            doNothing()
                    .when(service)
                    .deleteById(CERTIFICATE_TEST_BUILDER.build().getId());

            webTestClient.delete()
                    .uri("/certificates" + CERTIFICATE_TEST_BUILDER.build().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GiftCertificate.class)
                    .isEqualTo(CERTIFICATE_TEST_BUILDER.build());
        }

    }

}
