package ru.clevertec.ecl.spring.builders.impls;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.spring.builders.TestBuilder;
import ru.clevertec.ecl.spring.entity.Order;
import ru.clevertec.ecl.spring.entity.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUser")
@With
public class UserTestBuilder implements TestBuilder<User> {

    private Long id = 1L;
    private String email = "Ivan";
    private String password = "1111";
    private List<Order> orders = List.of(
            OrderTestBuilder.aOrder().build()
    );

    @Override
    public User build() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .orders(orders)
                .build();
    }
}
