package ru.clevertec.ecl.spring.builders.impls;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.spring.builders.TestBuilder;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrder")
@With
public class OrderTestBuilder implements TestBuilder<Order> {

    private Long id = 1L;
    private double price = 1.00;
    private LocalDateTime date_purchase = LocalDateTime.now();
    private LocalDateTime last_update_order = LocalDateTime.now();
    private User user = UserTestBuilder.aUser().build();
    private Set<GiftCertificate> certificates = Set.of(
            GiftCertificateTestBuilder.aCertificate().build()
    );

    @Override
    public Order build() {
        return Order.builder()
                .id(id)
                .price(price)
                .date_purchase(date_purchase)
                .last_update_order(last_update_order)
                .user(user)
                .certificates(certificates)
                .build();
    }
}
