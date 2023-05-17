package ru.clevertec.ecl.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query(value = "FROM Tag WHERE name LIKE % ?1 %", nativeQuery = true)
    Tag findByName(String tagName);
}
