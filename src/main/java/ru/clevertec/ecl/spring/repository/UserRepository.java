package ru.clevertec.ecl.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.spring.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
