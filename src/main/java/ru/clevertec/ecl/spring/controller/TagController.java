package ru.clevertec.ecl.spring.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.services.TagService;

/**
 * Класс контроллер для обработки запросов о теге
 */
@Slf4j
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов тэгов
     * @see TagService
     */
    private final TagService service;

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public Page<Tag> getTags(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                              @RequestParam(value = "limit", defaultValue = "10") @Min(1) Integer limit,
                              @RequestParam(value = "sort", defaultValue = "id,ASC")@NotEmpty String sort)
    {
        log.info("Get all tags with offset: {}, limit: {}, sort: {}", offset, limit, sort);

        return service.getAll(offset, limit, sort);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Tag getTagById(@PathVariable Long id){
        log.info("Find tag by id: {}", id);

        return service.getById(id);
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.FOUND)
    public Tag getTagByName(@PathVariable("name") String name){
        log.info("Find tag by name: {}", name);

        return service.getByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag createTag(@RequestBody @Valid Tag tag)
    {
        service.create(tag);
        log.info("Info tag - name: {} ", tag.getName());

        return tag;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Tag updateTag(@RequestBody @Valid Tag tag){
        service.update(tag);
        log.info("Info tag - name: {} ", tag.getName());

        return tag;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Tag deleteTag(@PathVariable Long id) {
        log.info("Delete tag by id: {}", id);

        return service.deleteById(id);
    }
}
