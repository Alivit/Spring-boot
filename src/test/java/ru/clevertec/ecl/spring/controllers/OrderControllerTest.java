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
import ru.clevertec.ecl.spring.builders.impls.OrderTestBuilder;
import ru.clevertec.ecl.spring.builders.impls.UserTestBuilder;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.services.OrderService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RequiredArgsConstructor
public class OrderControllerTest {

    @MockBean
    OrderService service;
    private final WebTestClient webTestClient;
    private final ObjectMapper mapper;
    private static final OrderTestBuilder ORDER_TEST_BUILDER = OrderTestBuilder.aOrder();
    private static final UserTestBuilder USER_TEST_BUILDER = UserTestBuilder.aUser();
    private static final GiftCertificateTestBuilder CERTIFICATE_TEST_BUILDER = GiftCertificateTestBuilder.aCertificate();

    @Nested
    public class FindByIdTest {

        @Test
        public void findByIdShouldReturnStatus200() {
            doReturn(ORDER_TEST_BUILDER)
                    .when(service)
                    .getById(ORDER_TEST_BUILDER.build().getId());

            webTestClient.get()
                    .uri("/orders" + ORDER_TEST_BUILDER.build().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Order.class)
                    .isEqualTo(ORDER_TEST_BUILDER.build());
        }

    }

    @Nested
    public class FindAllTest {

        @Test
        public void findAllShouldReturnStatus200() throws JsonProcessingException {
            String jsonArray = mapper.writeValueAsString(List.of(ORDER_TEST_BUILDER.build()));

            doReturn(List.of(ORDER_TEST_BUILDER.build()))
                    .when(service)
                    .getAll(any(Pageable.class));

            webTestClient.get()
                    .uri("/orders?page=0&size=10&sort=id,asc")
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
                    .uri("/orders?page=0&size=10&sort=id,asc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

    }

    @Nested
    public class CreateOrderTest {

        @Test
        public void createOrderShouldReturnStatus201() {

            doReturn(ORDER_TEST_BUILDER.build())
                    .when(service)
                    .create(ORDER_TEST_BUILDER.build(),
                            USER_TEST_BUILDER.build().getId(),
                            CERTIFICATE_TEST_BUILDER.build().getId());

            webTestClient.post()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ORDER_TEST_BUILDER.build())
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(Order.class)
                    .isEqualTo(ORDER_TEST_BUILDER.build());
        }

    }

    @Nested
    public
    class UpdateOrderTest {

        @Test
        public void updateOrderShouldReturnStatus200() {

            doReturn(ORDER_TEST_BUILDER.build())
                    .when(service)
                    .update(ORDER_TEST_BUILDER.build(),
                            USER_TEST_BUILDER.build(),
                            CERTIFICATE_TEST_BUILDER.build());

            webTestClient.put()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ORDER_TEST_BUILDER.build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Order.class)
                    .isEqualTo(ORDER_TEST_BUILDER.build());
        }

    }

    @Nested
    public class DeleteOrderTest {

        @Test
        public void deleteOrderShouldReturnStatus200() {

            doNothing()
                    .when(service)
                    .deleteById(ORDER_TEST_BUILDER.build().getId());

            webTestClient.delete()
                    .uri("/orders" + ORDER_TEST_BUILDER.build().getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Order.class)
                    .isEqualTo(ORDER_TEST_BUILDER.build());
        }

    }
}
