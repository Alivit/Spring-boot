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
import ru.clevertec.ecl.spring.builders.impls.TagTestBuilder;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.services.TagService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class TagControllerTest {

    @MockBean
    TagService service;
    private final WebTestClient webTestClient;
    private final ObjectMapper mapper;
    private static final TagTestBuilder TAG_TEST_BUILDER = TagTestBuilder.aTag();

    @Nested
    public class FindByIdTest {

        @Test
        public void findByIdShouldReturnStatus200() {
            doReturn(TAG_TEST_BUILDER)
                    .when(service)
                    .getById(TAG_TEST_BUILDER.build().getId());

            webTestClient.get()
                    .uri("/tags" + TAG_TEST_BUILDER.build().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Tag.class)
                    .isEqualTo(TAG_TEST_BUILDER.build());
        }

    }

    @Nested
    public class FindAllTest {

        @Test
        public void findAllShouldReturnStatus200() throws JsonProcessingException {
            String jsonArray = mapper.writeValueAsString(List.of(TAG_TEST_BUILDER.build()));

            doReturn(List.of(TAG_TEST_BUILDER.build()))
                    .when(service)
                    .getAll(any(Pageable.class));

            webTestClient.get()
                    .uri("/tags?page=0&size=10&sort=id,asc")
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
                    .uri("/tags?page=0&size=10&sort=id,asc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

    }

    @Nested
    public class CreateTagTest {

        @Test
        public void createTagShouldReturnStatus201() {

            doReturn(TAG_TEST_BUILDER.build())
                    .when(service)
                    .create(TAG_TEST_BUILDER.build());

            webTestClient.post()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(TAG_TEST_BUILDER.build())
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(Tag.class)
                    .isEqualTo(TAG_TEST_BUILDER.build());
        }

    }

    @Nested
    public
    class UpdateTagTest {

        @Test
        public void updateTagShouldReturnStatus200() {

            doReturn(TAG_TEST_BUILDER.build())
                    .when(service)
                    .update(TAG_TEST_BUILDER.build());

            webTestClient.put()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(TAG_TEST_BUILDER.build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Tag.class)
                    .isEqualTo(TAG_TEST_BUILDER.build());
        }

    }

    @Nested
    public class DeleteTagTest {

        @Test
        public void deleteTagShouldReturnStatus200() {

            doNothing()
                    .when(service)
                    .deleteById(TAG_TEST_BUILDER.build().getId());

            webTestClient.delete()
                    .uri("/tags" + TAG_TEST_BUILDER.build().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Tag.class)
                    .isEqualTo(TAG_TEST_BUILDER.build());
        }

    }
}
