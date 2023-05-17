package ru.clevertec.ecl.spring.entity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.clevertec.ecl.spring.entity.listener.OrderListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс хранящий информацию о заказе пользователя
 */
@Entity
@EntityListeners(OrderListener.class)
@Table(name = "orders")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class Order {

    @Id
    @Positive(message = "Id must be greater than 0")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;

    @Positive(message = "Price must be greater than 0.00 or 0")
    @Pattern(regexp = "\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})")
    private double price;

    private LocalDateTime date_purchase;
    private LocalDateTime last_update_order;

    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH, CascadeType.MERGE,
                            CascadeType.REFRESH, CascadeType.PERSIST
                    },
            targetEntity = User.class)
    @JoinTable(name = "orders_gift_certificates",
            inverseJoinColumns = @JoinColumn(name = "certificate_id",
                    nullable = false,
                    updatable = false),
            joinColumns = @JoinColumn(name = "order_id",
                    nullable = false,
                    updatable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<GiftCertificate> certificates = new HashSet<>();
}
