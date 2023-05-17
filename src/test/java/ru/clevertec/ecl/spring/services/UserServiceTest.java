package ru.clevertec.ecl.spring.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.spring.builders.impls.UserTestBuilder;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.repository.UserRepository;
import ru.clevertec.ecl.spring.services.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    UserService service;
    private final UserTestBuilder USER_TEST_BUILDER = UserTestBuilder.aUser();

    @BeforeEach
    void init() {
        service = new UserServiceImpl(userRepository);
    }
    @Nested
    class GetAllTest {

        @Test
        void getAllShouldReturnListOfSizeOne() {

            int expectedSize = 1;
            Pageable pageable = PageRequest.of(1,1);
            Page<User> page = new PageImpl<>(List.of(USER_TEST_BUILDER.build()));

            doReturn(page)
                    .when(userRepository)
                    .findAll(any(PageRequest.class));

            Page<User> actualValues = service.getAll(pageable);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        void testShouldReturnListWithContainsUser() {
            User expected = USER_TEST_BUILDER.build();
            Pageable pageable = PageRequest.of(1,1);
            Page<User> page = new PageImpl<>(List.of(expected));

            doReturn(page)
                    .when(userRepository)
                    .findAll(any(PageRequest.class));

            Page<User> actualValues = service.getAll(pageable);

            assertThat(actualValues).contains(expected);
        }

        @Test
        void testShouldReturnException() {
            Pageable pageable = PageRequest.of(1,1);

            doThrow(new NotFoundException(""))
                    .when(userRepository)
                    .findAll(any(PageRequest.class));

            assertThrows(NotFoundException.class, () -> service.getAll(pageable));
        }
    }

    @Nested
    class FindByIdTest {

        @Test
        void FindByIdShouldReturnUser() {
            User expected = USER_TEST_BUILDER.build();
            long id = expected.getId();

            doReturn(Optional.of(expected))
                   .when(userRepository)
                   .findById(id);


            User actual = service.getById(id);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void findByIdShouldReturnThrowException() {
            long id = 2L;

            doThrow(new NotFoundException(""))
                    .when(userRepository)
                    .findById(id);

            assertThrows(NotFoundException.class, () -> userRepository.findById(id));
        }

        @Test
        void findByIdShouldReturnThrowExceptionWithMessage() {
            long id = 5L;
            String expectedError = "User with id - " + id + " not found";

            doThrow(new NotFoundException(expectedError))
                    .when(userRepository)
                    .findById(id);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> userRepository.findById(id));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }
}
