package ru.clevertec.ecl.spring.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


/**
 * Класс хранящий информацию о тегах
 */
@Entity
@Table(name = "tags")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class Tag {

    @Id
    @Positive(message = "Id must be greater than 0")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank(message = "Name cannot be empty or null")
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH,CascadeType.MERGE,
                            CascadeType.REFRESH,CascadeType.PERSIST
                    },
            targetEntity = GiftCertificate.class)
    @JoinTable(name = "gift_certificates_tags",
            joinColumns = @JoinColumn(name = "tag_id",
                    nullable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "certificate_id",
                    nullable = false,
                    updatable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<GiftCertificate> certificate = new HashSet<>();

}
