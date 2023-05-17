package ru.clevertec.ecl.spring.services.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.exception.ServerErrorException;
import ru.clevertec.ecl.spring.repository.TagRepository;
import ru.clevertec.ecl.spring.services.TagService;

/**
 * Класс сервис crud операций для работы с тэгами
 */
@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    /**
     * Метод созданный для добавление данных в таблицу tags
     *
     * @param tag содержит данные о теге
     */
    @Override
    @Transactional
    public Tag create(@Valid Tag tag) {
        try {
            repository.save(tag);
            log.info("Insert tag - name: {} ", tag.getName());
            return getById(tag.getId());
        }catch (Exception e){
            throw new ServerErrorException("Error with Insert tag: " + e);
        }
    }

    /**
     * Метод который получает данные из таблицы tags в виде страницы
     * offset - начало страницы, limit - конец страницы
     * и сортирующий страницу по имени поля и по методу ASC/DESC
     *
     * @param pageable содержит информацию о страницы
     *
     * @return страницу тегов со всей информацией
     */
    @Override
    public Page<Tag> getAll(Pageable pageable) {
        Page<Tag> tags = repository.findAll(pageable);
        if(tags.isEmpty()) throw new NotFoundException("Tags not found");
        log.info("Tags : {}", tags);

        return tags;
    }

    /**
     * Метод созданный для обновлении данных о теге в таблице tags
     *
     * @param tag содержит данные о теге
     */
    @Override
    @Transactional
    public Tag update(@Valid Tag tag) {
        try {
            Tag tagOld = repository.getReferenceById(tag.getId());
            tagOld.setName(tag.getName());
            repository.save(tag);
            log.info("Update tag - name: {} ", tag.getName());

            return getById(tag.getId());
        }catch (Exception e){
            throw new ServerErrorException("Error with Update tag: " + e);
        }
    }

    /**
     * Метод созданный для удаления данных из таблицы tags по id
     *
     * @param id содержит id тега
     *
     * @return тега со всей информацией
     */
    @Override
    @Transactional
    public Tag deleteById(@Positive Long id) {
        try {
            Tag tag = getById(id);
            repository.delete(tag);
            log.info("Teg with id " + id + " was deleted");
            return tag;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new ServerErrorException("Error with Delete tag: " + e);
        }
    }

    /**
     * Метод который находит данные из таблицы tags по id
     *
     * @param id содержит id тега
     *
     * @return тега со всей информацией
     */
    @Override
    public Tag getById(@Positive Long id) {
        Tag tag = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Tag with id - " + id + " not found"));
        log.info("Tag with id " + id + ": {}", tag);

        return tag;
    }

    /**
     * Метод который находит данные из таблицы tags по названию
     *
     * @param name содержит название тега
     *
     * @return тега со всей информацией
     */
    @Override
    public Tag getByName(@NotBlank String name) {
        Tag tag = repository.findByName(name);
        if(tag == null) throw new NotFoundException("Tag with name - " + name + " not found");
        log.info("Tag with name " + name + ": {}", tag);

        return tag;
    }

}
