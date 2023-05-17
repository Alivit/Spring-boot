package ru.clevertec.ecl.spring.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.entity.Tag;

public interface TagService {

    public Tag create(@Valid Tag tag);

    public Page<Tag> getAll(Pageable pageable);

    public Tag update(@Valid Tag tag);

    public Tag deleteById(@Positive Long id);

    public Tag getById(@Positive Long id);

    public Tag getByName(@NotBlank String name);

}
