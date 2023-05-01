package ru.clevertec.ecl.spring.repository;

import ru.clevertec.ecl.spring.entities.Tag;

import java.util.List;

public interface TagRepository { //тут можно extends и от бд в моем случае extends MongoRepository<'Сюда название модели', String> и тогда оно автоматом эти методы возьмет

    int add(List<String> names, List<Double> prices, List<Integer> durations, String tag);

    List<Tag> getAll();

    int update(List<String> names, List<Double> prices, List<Integer> durations, String tagName);

    int remove(Tag tag);

    Tag getById(Long id);

    List<Tag> findByName(String name);

    Tag getById(Long id, String sort, List<String> sortBy);

    List<Tag> findByName(String name, String sort, List<String> sortBy);
}
