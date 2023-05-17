package ru.clevertec.ecl.spring.builders.impls;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.spring.builders.TestBuilder;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Tag;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aTag")
@With
public class TagTestBuilder implements TestBuilder<Tag> {

    private Long id = 1L;
    private String name = "giftTag";
    private Set<GiftCertificate> certificates = Set.of(
            GiftCertificateTestBuilder.aCertificate().build()
    );

    @Override
    public Tag build() {
        return Tag.builder()
                .id(id)
                .name(name)
                .certificate(certificates)
                .build();
    }
}
