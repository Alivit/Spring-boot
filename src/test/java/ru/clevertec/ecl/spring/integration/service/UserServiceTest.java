package ru.clevertec.ecl.spring.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.integration.BaseIntegrationTest;
import ru.clevertec.ecl.spring.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    private UserService service;

    @Test
    void findAllShouldReturn3(){
        Pageable pageable = PageRequest.of(0, 3, Sort.by("id"));
        int expectedUsersCount = 3;
        Page<User> actual = service.getAll(pageable);

        assertThat(actual.getTotalPages()).isEqualTo(expectedUsersCount);

    }

    @Test
    void findWithId2(){
        int expectedUsersId = 2;

        User actual = service.getById(2L);

        assertThat(actual.getId()).isEqualTo(expectedUsersId);
    }

}
