package ru.clevertec.ecl.spring.repository;

import ru.clevertec.ecl.spring.entities.Tag;

import java.util.List;

public interface TagRepository {

    void add(Tag tag);

    List<Tag> getAll();

    void update(Tag tag);

    void remove(Tag tag);

    Tag getById(Long id);

    List<Tag> findByName(String name);
}
