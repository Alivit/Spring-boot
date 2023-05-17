package ru.clevertec.ecl.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<Page<Tag>> getTags(Pageable pageable) {
        log.info("Request: get all tags with Pageable: {}", pageable.toString());

        Page<Tag> response = service.getAll(pageable);
        log.info("Response: received number of tags - {}", response.getTotalPages());

        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id){
        log.info("Request: find tag by id: {}", id);

        Tag response = service.getById(id);
        log.info("Response: found tag: {}", response.toString());

        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }

    @GetMapping("/name")
    public ResponseEntity<Tag> getTagByName(@RequestParam String name){
        log.info("Request: find tag by name: {}", name);

        Tag response = service.getByName(name);
        log.info("Response: found tag: {}", response.toString());

        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(Tag tag) {
        log.info("Request: info tag: {}", tag.toString());

        Tag response = service.create(tag);
        log.info("Response: created tag: {}", response.toString());

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Tag> updateTag(Tag tag){
        log.info("Request: info tag: {}", tag.toString());

        Tag response = service.update(tag);
        log.info("Response: updated tag: {}", response.toString());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable Long id) {
        log.info("Request: delete tag by id: {}", id);

        Tag response = service.deleteById(id);
        log.info("Response: deleted tag: {}", response.toString());

        return ResponseEntity.ok(response);
    }
}
