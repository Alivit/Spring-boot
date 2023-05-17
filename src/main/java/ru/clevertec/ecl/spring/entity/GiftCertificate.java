package ru.clevertec.ecl.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import ru.clevertec.ecl.spring.entity.listener.GiftCertificateListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * Класс хранящий информацию о подарочных сертификатах
 */
@Entity
@EntityListeners(GiftCertificateListener.class)
@Table(name = "gift_certificates")
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class GiftCertificate {

    @Id
    @Positive(message = "Id must be greater than 0")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank(message = "Name cannot be empty or null")
    private String name;

    @Positive(message = "Price must be greater than 0.00 or 0")
    @Pattern(regexp = "\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})")
    private double price;

    @Positive(message = "Duration must be greater than 0")
    @Column(name = "duration")
    private int duration;

    private LocalDateTime create_date;
    private LocalDateTime last_update_data;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH, CascadeType.MERGE,
                            CascadeType.REFRESH, CascadeType.PERSIST
                    },
            targetEntity = Tag.class
    )
    @JoinTable(name = "gift_certificates_tags",
            inverseJoinColumns = @JoinColumn(name = "tag_id",
                    nullable = false,
                    updatable = false),
            joinColumns = @JoinColumn(name = "certificate_id",
                    nullable = false,
                    updatable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags = new HashSet<>();

}
