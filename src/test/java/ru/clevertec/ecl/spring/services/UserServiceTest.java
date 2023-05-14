package ru.clevertec.ecl.spring.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.springframework.data.domain.Page;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.repository.UserRepository;
import ru.clevertec.ecl.spring.services.impl.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    UserService service;

    @BeforeEach
    void init() {
        service = new UserServiceImpl(userRepository);
    }

    @Test
    public void getAllTest(){
        Page<User> userList = service.getAll(4, 5, "id:ASC");

        assertThat(userList.getTotalPages()).isEqualTo(5);
    }
}
