package ru.clevertec.ecl.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.ecl.spring.entities.GiftCertificate;
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
    public ResponseEntity<?> getTagWithId(@PathVariable Long id){
        Tag tag = tagService.getById(id);

        if(tag == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Tag with id " + id + " nor found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping("/tags/name/{name}")
    public ResponseEntity<?> getTagWithId(@PathVariable String name){
        List<Tag> tags = tagService.findByName(name);

        if(tags.size() == 0) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Tag with name " + name + " nor found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

}
