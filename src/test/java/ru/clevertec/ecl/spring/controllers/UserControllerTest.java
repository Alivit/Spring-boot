package ru.clevertec.ecl.spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.spring.builders.impls.UserTestBuilder;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class UserControllerTest {

    @MockBean
    private UserService service;
    private final WebTestClient webTestClient;
    private final ObjectMapper mapper;
    private static final UserTestBuilder USER_TEST_BUILDER = UserTestBuilder.aUser();

    @Nested
    class FindByIdTest{

        @Test
        void findByIdShouldReturnStatus200(){

            doReturn(USER_TEST_BUILDER)
                    .when(service)
                    .getById(USER_TEST_BUILDER.build().getId());

            webTestClient.get()
                    .uri("/users/" + USER_TEST_BUILDER.build().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(User.class)
                    .isEqualTo(USER_TEST_BUILDER.build());
        }

    }

    @Nested
    class FindAllTest {

        @Test
        void testShouldReturnStatus200() throws JsonProcessingException {
            String json = mapper.writeValueAsString(List.of(USER_TEST_BUILDER.build()));

            doReturn(List.of(USER_TEST_BUILDER.build()))
                    .when(service)
                    .getAll(any(Pageable.class));

            webTestClient.get()
                    .uri("/users?page=0&size=10&sort=id,asc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json(json);
        }

        @Test
        void findAllShouldReturnEmptyJson() {
            doReturn(List.of())
                    .when(service)
                    .getAll(any(Pageable.class));

            webTestClient.get()
                    .uri("/users?page=0&size=10&sort=id,asc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }
    }

}
