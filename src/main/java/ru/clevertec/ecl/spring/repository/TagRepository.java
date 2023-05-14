package ru.clevertec.ecl.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("FROM Tag WHERE name LIKE % : tagName %")
    Tag findByName(String tagName);
}
