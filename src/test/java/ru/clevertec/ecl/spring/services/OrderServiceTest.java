package ru.clevertec.ecl.spring.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.spring.builders.impls.OrderTestBuilder;
import ru.clevertec.ecl.spring.builders.impls.TagTestBuilder;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.exception.ServerErrorException;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;
import ru.clevertec.ecl.spring.repository.OrderRepository;
import ru.clevertec.ecl.spring.repository.UserRepository;
import ru.clevertec.ecl.spring.services.impl.OrderServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private UserRepository userRepository;

    OrderService service;

    private final OrderTestBuilder ORDER_TEST_BUILDER = OrderTestBuilder.aOrder();


    private static Stream<Arguments> orderArgumentsProvider(){
        return Stream.of(
                Arguments.of(new Order(),
                        new GiftCertificate(3, "gift1", 120, 45, null, null, null),
                        new User(3L,"vitaliy", "12345", null))
        );
    }

    private static Stream<Arguments> orderArgumentsProviderUpdate(){
        return Stream.of(
                Arguments.of(new Order(),
                        new GiftCertificate(3, "newGift", 120, 45, null, null, null), new User(),
                        new User(3L,"artur", "1111", null))
        );
    }

    @BeforeEach
    void init() {
        service = new OrderServiceImpl(orderRepository, certificateRepository, userRepository);
    }

    @Nested
    class CreateOrderTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.OrderServiceTest#orderArgumentsProvider")
        public void addTest(Order order, User user, GiftCertificate certificate) {
            service.create(order, certificate.getId(), user.getId());

            assertThat(service.getById(order.getId()).getCertificates()).isNotNull();
        }

        @Test
        void createOrderShouldReturnException() {
            Order actual = ORDER_TEST_BUILDER.build();
            String expectedError = "Error with Created order: ";

            lenient()
                    .doThrow(new ServerErrorException(expectedError))
                    .when(orderRepository)
                    .save(actual);

            Exception exception = assertThrows(ServerErrorException.class,
                    () -> service.create(actual, 1L, 1L));
            String actualError = exception.getMessage();

            assertThat(actualError).contains(expectedError);
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        void FindByIdShouldReturnOrder() {
            Order expected = ORDER_TEST_BUILDER.build();
            long id = expected.getId();

            doReturn(Optional.of(expected))
                    .when(orderRepository)
                    .findById(id);


            Order actual = service.getById(id);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void findByIdShouldReturnThrowException() {
            long id = 2L;

            doThrow(new NotFoundException(""))
                    .when(orderRepository)
                    .findById(id);

            assertThrows(NotFoundException.class, () -> service.getById(id));
        }

        @Test
        void findByIdShouldReturnThrowExceptionWithMessage() {
            long id = 5L;
            String expectedError = "Order with id - " + id + " not found";

            doThrow(new NotFoundException(expectedError))
                    .when(orderRepository)
                    .findById(id);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> orderRepository.findById(id));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }

    @Nested
    class GetAllTest {

        @Test
        void getAllShouldReturnListOfSizeOne() {

            int expectedSize = 1;
            Pageable pageable = PageRequest.of(1,1);
            Page<Order> page = new PageImpl<>(List.of(ORDER_TEST_BUILDER.build()));

            doReturn(page)
                    .when(orderRepository)
                    .findAll(any(PageRequest.class));

            Page<Order> actualValues = service.getAll(pageable);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        void testShouldReturnListWithContainsOrder() {
            Order expected = ORDER_TEST_BUILDER.build();
            Pageable pageable = PageRequest.of(1,1);
            Page<Order> page = new PageImpl<>(List.of(expected));

            doReturn(page)
                    .when(orderRepository)
                    .findAll(any(PageRequest.class));

            Page<Order> actualValues = service.getAll(pageable);

            assertThat(actualValues).contains(expected);
        }

        @Test
        void testShouldReturnException() {
            Pageable pageable = PageRequest.of(1,1);

            doThrow(new NotFoundException(""))
                    .when(orderRepository)
                    .findAll(any(PageRequest.class));

            assertThrows(NotFoundException.class, () -> service.getAll(pageable));
        }
    }

    @Nested
    class DeleteOrderTest{

        @ParameterizedTest
        @ValueSource(ints = {1,2})
        public void removeTest(int id) {
            service.deleteById((long) id);

            assertThat(service.getById((long) id)).isNull();
        }

        @Test
        void deleteOrderShouldReturnException() {
            long id = ORDER_TEST_BUILDER.build().getId();
            String expectedError = "Order with id - " + id + " not found";

            lenient()
                    .doThrow(new NotFoundException(expectedError))
                    .when(orderRepository)
                    .deleteById(id);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> service.deleteById(id));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }

    @Nested
    class UpdateOrderTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.OrderServiceTest#orderArgumentsProviderUpdate")
        public void updateTest(Order order, GiftCertificate certificate, User user){

            service.update(order, user, certificate);

            assertThat(service.getById(order.getId()).getUser().getEmail()).isEqualTo("artur");
        }

        @Test
        void updateOrderShouldReturnException() {
            Order actual = ORDER_TEST_BUILDER.build();
            String expectedError = "Error with Update";

            lenient()
                    .doThrow(new ServerErrorException(expectedError))
                    .when(orderRepository)
                    .save(actual);

            Exception exception = assertThrows(ServerErrorException.class,
                    () -> service.update(actual, null, null));
            String actualError = exception.getMessage();

            assertThat(actualError).contains(expectedError);
        }

    }
}
