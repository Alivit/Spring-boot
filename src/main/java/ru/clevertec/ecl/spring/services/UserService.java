package ru.clevertec.ecl.spring.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.entity.User;

import java.util.List;

public interface UserService {

    public User getById(Long id);

    public Page<User> getAll(Pageable pageable);
}
