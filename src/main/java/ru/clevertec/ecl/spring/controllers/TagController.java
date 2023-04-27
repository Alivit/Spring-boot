package ru.clevertec.ecl.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.clevertec.ecl.spring.entities.Tag;
import ru.clevertec.ecl.spring.exception.AppError;
import ru.clevertec.ecl.spring.services.TagService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<?> tagsInfo(){
        List<Tag> tags = tagService.getAll();

        if(tags.size() == 0){
            return new ResponseEntity<>(new AppError(HttpStatus.NO_CONTENT.value(),
                    "Gift certificates not founded"), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tags/id/{id}")
    public ResponseEntity<?> getTagWithId(@PathVariable Long id, @RequestParam("sort") String sort,
                                          @RequestParam("sortBy") List<String> sortBy){
        Tag tag;

        if(sort != null && sortBy != null) {
            tag = tagService.getById(id, sort, sortBy);
        }
        else {
            tag = tagService.getById(id);
        }

        if(tag == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Tag with id " + id + " nor found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("/tags/name/{name}")
    public ResponseEntity<?> getTagWithName(@PathVariable("name") String name, @RequestParam("sort") String sort,
                                          @RequestParam("sortBy") List<String> sortBy){
        List<Tag> tags;

        if(sort != null && sortBy != null) {
            tags = tagService.findByName(name, sort, sortBy);
        }
        else {
            tags = tagService.findByName(name);
        }

        if(tags.size() == 0) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Tag with name " + name + " nor found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PostMapping("/tags")
    public ResponseEntity<?> createTags(@RequestParam("name") List<String> names, @RequestParam("price") List<Double> prices,
                           @RequestParam("duration") List<Integer> durations,@RequestParam("tag") String tag){
        if(tagService.add(names,prices,durations,tag) == 1){
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Gift certificate with id " + tag + " nor created"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PutMapping("/tags")
    public ResponseEntity<?> updateTag(@RequestParam("name") List<String> names, @RequestParam("price") List<Double> prices,
                                               @RequestParam("duration") List<Integer> durations,@RequestParam("tag") String tag){
        if(tagService.update(names,prices,durations,tag) == 1){
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Gift certificate with id " + tag + " nor updated"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        Tag tag = tagService.getById(id);

        if(tag == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Tag with id " + id + " nor found"), HttpStatus.NOT_FOUND);
        }

        if(tagService.remove(tag) == 1){
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Tag with id " + id + " nor deleted"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }
}
