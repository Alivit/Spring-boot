package ru.clevertec.ecl.spring.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов sql
     * @see TagRepository
     */
    private final TagRepository repository;

    /**
     * Метод созданный для добавление данных в таблицу tags
     *
     * @param tag содержит данные о теге
     */
    @Override
    @Transactional
    public void create(Tag tag) {
        try {
            repository.save(tag);
            log.info("Insert tag - name: {} ", tag.getName());
        }catch (Exception e){
            throw new ServerErrorException("Error with Insert tag: " + e);
        }
    }

    /**
     * Метод который получает данные из таблицы tags в виде страницы
     * offset - начало страницы, limit - конец страницы
     * и сортирующий страницу по имени поля и по методу ASC/DESC
     *
     * @param offset содержит начало страницы
     * @param limit содержит конец страницы
     * @param sort лист с обьектами по которым надо сортировать
     *
     * @return страницу тегов со всей информацией
     */
    @Override
    public Page<Tag> getAll(Integer offset, Integer limit, String sort) {
        String[] sorting = sort.split(",");

        Page<Tag> tags = repository.findAll(
                PageRequest.of(offset,limit, sortSetting(sorting[0], sorting[1])));
        if(tags.isEmpty()) throw new NotFoundException("Tags not found");
        log.info("Tags : {}", tags);

        return tags;
    }

    /**
     * Метод который настраивает сортировку по имени поля и по методу ASC/DESC
     *
     * @param sortField содержит имя поле
     * @param sortMethod содержит метод сортировки ASC/DESC
     *
     * @return объект сортировки
     */
    private Sort sortSetting(String sortField, String sortMethod){
        if("ASC".equals(sortMethod)) {
            return Sort.by(Sort.Direction.ASC, sortField);
        }
        else {
            return Sort.by(Sort.Direction.DESC, sortField);
        }
    }


    /**
     * Метод созданный для обновлении данных о теге в таблице tags
     *
     * @param tag содержит данные о теге
     */
    @Override
    @Transactional
    public void update(Tag tag) {
        try {
            Tag tagOld = repository.getReferenceById(tag.getId());
            tagOld.setName(tag.getName());
            repository.save(tag);
            log.info("Update tag - name: {} ", tag.getName());
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
    public Tag deleteById(Long id) {
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
    public Tag getById(Long id) {
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
    public Tag getByName(String name) {
        Tag tag = repository.findByName(name);
        if(tag == null) throw new NotFoundException("Tag with name - " + name + " not found");
        log.info("Tag with name " + name + ": {}", tag);

        return tag;
    }

}
