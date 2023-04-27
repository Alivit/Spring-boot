package ru.clevertec.ecl.spring.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "gift_certificate")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "duration")
    private int duration;

    @Column(name = "create_date")
    private LocalDateTime create_date;

    @Column(name = "last_update_data")
    private LocalDateTime last_update_data;

    @PrePersist
    private void createDate(){
        create_date = last_update_data = LocalDateTime.now();
    }

    @PreUpdate
    private void updateDate(){
        last_update_data = LocalDateTime.now();
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            })
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private Set<Tag> tags = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate that)) return false;
        return getId() == that.getId() && Double.compare(that.getPrice(), getPrice()) == 0 && getDuration() == that.getDuration() && Objects.equals(getName(), that.getName()) && Objects.equals(getCreate_date(), that.getCreate_date()) && Objects.equals(getLast_update_data(), that.getLast_update_data());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getDuration(), getCreate_date(), getLast_update_data());
    }
}
