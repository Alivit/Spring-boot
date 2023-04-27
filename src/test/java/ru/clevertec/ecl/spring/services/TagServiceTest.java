package ru.clevertec.ecl.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.spring.entities.GiftCertificate;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TagServiceTest {

    TagService tagsService;

    @BeforeEach
    void init(){
        tagsService = new TagService();
    }
    @Test
    public void addTagsTest(){
        Set<String> setTags = Set.of("blue", "red");

        GiftCertificate certificate1 = new GiftCertificate();
        certificate1.setName("gift1");
        certificate1.setPrice(100);
        certificate1.setDuration(90);



        assertThat(tagsService.getAll().size()).isEqualTo(1);

    }
}
