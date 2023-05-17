package ru.clevertec.ecl.spring.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.repository.TagRepository;
import ru.clevertec.ecl.spring.services.impl.TagServiceImpl;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;
    TagService service;

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

    @ParameterizedTest
    @MethodSource("tagsArgumentsProvider")
    public void addTest(Tag tag) {
        service.create(tag);

        assertThat(service.getByName("pink")).isNotNull();
    }

    @Test
    public void getAllTest(){
        Pageable pageable = PageRequest.of(0, 3, Sort.by("id"));
        Page<Tag> tagList = service.getAll(pageable);

        assertThat(tagList.getTotalPages()).isEqualTo(3);
    }

    @ParameterizedTest
    @MethodSource("tagsArgumentsProvider")
    public void removeTest(Tag tag) {
        service.deleteById(tag.getId());

        assertThat(service.getByName(tag.getName())).isNull();
    }

    @ParameterizedTest
    @MethodSource("tagsArgumentsProviderUpdate")
    public void updateTest(Tag tag) {
        service.update(tag);

        assertThat(service.getByName("yellow")).isNotNull();
    }
}
