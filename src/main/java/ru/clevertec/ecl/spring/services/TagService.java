package ru.clevertec.ecl.spring.services;

import org.springframework.data.domain.Page;
import ru.clevertec.ecl.spring.entity.Tag;

public interface TagService {

    public void create(Tag tag);

    public Page<Tag> getAll(Integer offset, Integer limit, String sort);

    public void update(Tag tag);

    public Tag deleteById(Long id);

    public Tag getById(Long id);

    public Tag getByName(String name);

}
