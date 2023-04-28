package ru.clevertec.ecl.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.ecl.spring.entities.Tag;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TagServiceTest {

    TagService tagsService;

    private static Stream<Arguments> tagsArgumentsProvider(){
        return Stream.of(
                Arguments.of("pink")
        );
    }

    private static Stream<Arguments> tagsArgumentsProviderUpdate(){
        return Stream.of(
                Arguments.of("yellow")
        );
    }

    @BeforeEach
    void init() {
        tagsService = new TagService();
    }

    @ParameterizedTest
    @MethodSource("tagsArgumentsProvider")
    public void addTest(String tagName) {
        List<String> names = List.of("gift1", "gift2");
        List<Double> prices = List.of(100.00, 200.00);
        List<Integer> durations = List.of(30, 45);

        tagsService.add(names,prices,durations,tagName);

        assertThat(tagsService.findByName("pink")).isNotNull();
    }

    @Test
    public void getAllTest(){
        List<Tag> tagList = tagsService.getAll();

        assertThat(tagList.size()).isNotEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("tagsArgumentsProvider")
    public void removeTest(String tagName) {
        Tag tag = new Tag(0, tagName, null);

        tagsService.remove(tag);

        assertThat(tagsService.findByName(tag.getName())).isNull();
    }

    @ParameterizedTest
    @MethodSource("tagsArgumentsProviderUpdate")
    public void updateTest(String tagName) {
        List<String> names = List.of("gift3", "gift4");
        List<Double> prices = List.of(130.00, 210.00);
        List<Integer> durations = List.of(20, 43);

        tagsService.update(names,prices,durations,tagName);

        assertThat(tagsService.findByName("yellow")).isNotNull();
    }
}
