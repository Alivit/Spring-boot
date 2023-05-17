package ru.clevertec.ecl.spring.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.entity.Tag;

public interface TagService {

    public Tag create(Tag tag);

    public Page<Tag> getAll(Pageable pageable);

    public Tag update(Tag tag);

    public Tag deleteById(Long id);

    public Tag getById(Long id);

    public Tag getByName(String name);

}
