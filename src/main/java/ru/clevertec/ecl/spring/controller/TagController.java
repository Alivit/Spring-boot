package ru.clevertec.ecl.spring.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    private final TagService service;

    @GetMapping
    public ResponseEntity<Page<Tag>> getTags(Pageable pageable)
    {
        log.info("Get all tags with page: {}, size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return new ResponseEntity<>(service.getAll(pageable),HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id){
        log.info("Find tag by id: {}", id);

        return new ResponseEntity<>(service.getById(id),HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public ResponseEntity<Tag> getTagByName(@RequestParam String name){
        log.info("Find tag by name: {}", name);

        return new ResponseEntity<>(service.getByName(name),HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody @Valid Tag tag) {
        log.info("Info tag - name: {} ", tag.getName());

        return new ResponseEntity<>(service.create(tag),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Tag> updateTag(@RequestBody @Valid Tag tag){
        log.info("Info tag - name: {} ", tag.getName());

        return ResponseEntity.ok(service.update(tag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable Long id) {
        log.info("Delete tag by id: {}", id);

        return ResponseEntity.ok(service.deleteById(id));
    }
}
