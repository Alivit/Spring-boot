package ru.clevertec.ecl.spring.builders.impls;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.spring.builders.TestBuilder;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Tag;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCertificate")
@With
public class GiftCertificateTestBuilder implements TestBuilder<GiftCertificate> {

    private Long id = 1L;
    private String name = "certificate";
    private double price = 1.00;
    private int duration = 2;
    private LocalDateTime create_date = LocalDateTime.now();
    private LocalDateTime last_update_data = LocalDateTime.now();
    private Set<Tag> tags = Set.of(
            TagTestBuilder.aTag().build(),
            TagTestBuilder.aTag().withName("green").build(),
            TagTestBuilder.aTag().withName("red").build()
    );

    @Override
    public GiftCertificate build() {
        return GiftCertificate.builder()
                .id(id)
                .name(name)
                .price(price)
                .duration(duration)
                .create_date(create_date)
                .last_update_data(last_update_data)
                .tags(tags)
                .build();
    }
}
