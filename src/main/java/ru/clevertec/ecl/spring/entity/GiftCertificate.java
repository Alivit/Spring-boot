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

import lombok.AllArgsConstructor;
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
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class GiftCertificate {

    /**
     * Поле с айди сертификата
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private long id;

    /**
     * Поле с названием сертификата
     */
    @Column(name = "name")
    private String name;

    /**
     * Поле с ценой сертификата
     */
    @Column(name = "price")
    private double price;

    /**
     * Поле с продолжительностью сертификата
     */
    @Column(name = "duration")
    private int duration;

    /**
     * Поле с датой создания сертификата
     */
    @Column(name = "create_date")
    private LocalDateTime create_date;

    /**
     * Поле с датой обновления сертификата
     */
    @Column(name = "last_update_data")
    private LocalDateTime last_update_data;

    /**
     * Поле со списком тегов
     */
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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Tag> tags = new HashSet<>();

}
