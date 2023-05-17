package ru.clevertec.ecl.spring.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.spring.builders.impls.TagTestBuilder;
import ru.clevertec.ecl.spring.builders.impls.UserTestBuilder;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.entity.User;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.exception.ServerErrorException;
import ru.clevertec.ecl.spring.repository.TagRepository;
import ru.clevertec.ecl.spring.services.impl.TagServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;
    TagService service;

    private final TagTestBuilder TAG_TEST_BUILDER = TagTestBuilder.aTag();

    private static Stream<Arguments> tagsArgumentsProvider(){
        return Stream.of(
                Arguments.of(new Tag(3, "pink", null))
        );
    }

    private static Stream<Arguments> tagsArgumentsProviderUpdate(){
        return Stream.of(
                Arguments.of(new Tag(3, "yellow", null))
        );
    }

    @BeforeEach
    void init() {
        service = new TagServiceImpl(tagRepository);
    }

    @Nested
    class CreateTagTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.TagServiceTest#tagsArgumentsProvider")
        public void addTest(Tag tag) {
            service.create(tag);

            assertThat(service.getByName("pink")).isNotNull();
        }

        @Test
        void createTagShouldReturnException() {
            Tag actual = TAG_TEST_BUILDER.build();
            String expectedError = "Error with Created tag: ";

            lenient()
                    .doThrow(new ServerErrorException(expectedError))
                    .when(tagRepository)
                    .save(actual);

            Exception exception = assertThrows(ServerErrorException.class,
                    () -> service.create(actual));
            String actualError = exception.getMessage();

            assertThat(actualError).contains(expectedError);
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        void FindByIdShouldReturnTag() {
            Tag expected = TAG_TEST_BUILDER.build();
            long id = expected.getId();

            doReturn(Optional.of(expected))
                    .when(tagRepository)
                    .findById(id);

            Tag actual = service.getById(id);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void findByIdShouldReturnThrowException() {
            long id = 2L;

            doThrow(new NotFoundException(""))
                    .when(tagRepository)
                    .findById(id);

            assertThrows(NotFoundException.class, () -> tagRepository.findById(id));
        }

        @Test
        void findByIdShouldReturnThrowExceptionWithMessage() {
            long id = 5L;
            String expectedError = "Tag with id - " + id + " not found";

            doThrow(new NotFoundException(expectedError))
                    .when(tagRepository)
                    .findById(id);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> tagRepository.findById(id));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }

    @Nested
    class FindByNameTest {

        @Test
        void FindByNameShouldReturnTag() {
            Tag expected = TAG_TEST_BUILDER.build();
            String name = expected.getName();

            doReturn(expected)
                    .when(tagRepository)
                    .findByName(name);


            Tag actual = service.getByName(name);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void findByNameShouldReturnThrowException() {
            String name = "default";

            doThrow(new NotFoundException(""))
                    .when(tagRepository)
                    .findByName(name);

            assertThrows(NotFoundException.class, () -> tagRepository.findByName(name));
        }

        @Test
        void findByNameShouldReturnThrowExceptionWithMessage() {
            String name = "default";
            String expectedError = "Tag with name - " + name + " not found";

            doThrow(new NotFoundException(expectedError))
                    .when(tagRepository)
                    .findByName(name);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> tagRepository.findByName(name));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }
    @Nested
    class GetAllTest {

        @Test
        void getAllShouldReturnListOfSizeOne() {

            int expectedSize = 1;
            Pageable pageable = PageRequest.of(1,1);
            Page<Tag> page = new PageImpl<>(List.of(TAG_TEST_BUILDER.build()));

            doReturn(page)
                    .when(tagRepository)
                    .findAll(any(PageRequest.class));

            Page<Tag> actualValues = service.getAll(pageable);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        void testShouldReturnListWithContainsTag() {
            Tag expected = TAG_TEST_BUILDER.build();
            Pageable pageable = PageRequest.of(1,1);
            Page<Tag> page = new PageImpl<>(List.of(expected));

            doReturn(page)
                    .when(tagRepository)
                    .findAll(any(PageRequest.class));

            Page<Tag> actualValues = service.getAll(pageable);

            assertThat(actualValues).contains(expected);
        }

        @Test
        void testShouldReturnException() {
            Pageable pageable = PageRequest.of(1,1);

            doThrow(new NotFoundException(""))
                    .when(tagRepository)
                    .findAll(any(PageRequest.class));

            assertThrows(NotFoundException.class, () -> service.getAll(pageable));
        }
    }

    @Nested
    class DeleteTagTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.TagServiceTest#tagsArgumentsProvider")
        public void deleteTest(Tag tag) {
            Tag expected = service.deleteById(tag.getId());

            assertThat(tag).isEqualTo(expected);
        }

        @Test
        void deleteTagShouldReturnException() {
            long id = TAG_TEST_BUILDER.build().getId();
            String expectedError = "Tag with id - " + id + " not found";

            lenient()
                    .doThrow(new NotFoundException(expectedError))
                    .when(tagRepository)
                    .deleteById(id);

            Exception exception = assertThrows(NotFoundException.class,
                    () -> service.deleteById(id));
            String actualError = exception.getMessage();

            assertThat(actualError).isEqualTo(expectedError);
        }

    }

    @Nested
    class UpdateTagTest{

        @ParameterizedTest
        @MethodSource("ru.clevertec.ecl.spring.services.TagServiceTest#tagsArgumentsProviderUpdate")
        public void updateTest(Tag tag) {
            service.update(tag);

            assertThat(service.getByName("yellow")).isNotNull();
        }

        @Test
        void updateTagShouldReturnException() {
            Tag actual = TAG_TEST_BUILDER.build();
            String expectedError = "Error with Update tag: ";

            lenient()
                    .doThrow(new ServerErrorException(expectedError))
                    .when(tagRepository)
                    .save(actual);

            Exception exception = assertThrows(ServerErrorException.class,
                    () -> service.update(actual));
            String actualError = exception.getMessage();

            assertThat(actualError).contains(expectedError);
        }

    }
}
