package ru.clevertec.ecl.spring.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;
import ru.clevertec.ecl.spring.repository.OrderRepository;
import ru.clevertec.ecl.spring.repository.UserRepository;
import ru.clevertec.ecl.spring.services.impl.OrderServiceImpl;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private UserRepository userRepository;

    OrderService service;


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

    @ParameterizedTest
    @MethodSource("orderArgumentsProvider")
    public void addTest(Order order, User user, GiftCertificate certificate) {
        service.create(order, certificate.getId(), user.getId());

        assertThat(service.getById(order.getId()).getCertificates()).isNotNull();
    }

    @Test
    public void getAllTest(){
        Page<Order> orderList = service.getAll(5, 10,"id:DESC");

        assertThat(orderList.getTotalPages()).isNotEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2})
    public void removeTest(int id) {
        service.deleteById((long) id);

        assertThat(service.getById((long) id)).isNull();
    }

    @ParameterizedTest
    @MethodSource("orderArgumentsProviderUpdate")
    public void updateTest(Order order, GiftCertificate certificate, User user){

        service.update(order, user, certificate);

        assertThat(service.getById(order.getId()).getUser().getEmail()).isEqualTo("artur");
    }
}
