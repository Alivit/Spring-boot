package ru.clevertec.ecl.spring.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.services.GiftCertificateService;

import java.util.Set;

/**
 * Класс контроллер для обработки запросов о подарочном сертификате
 */
@Slf4j
@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService service;

    @GetMapping
    public ResponseEntity<Page<GiftCertificate>> getCertificates(Pageable pageable)
    {
        log.info("Get all certificates with page: {}, size: {}, sort: {}",
                pageable.getPageNumber(),pageable.getPageNumber(), pageable.getSort());

        return new ResponseEntity<>(service.getAll(pageable), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> getCertificateById(@PathVariable Long id){
        log.info("Find certificate by id: {}", id);

        return new ResponseEntity<>(service.getById(id), HttpStatus.FOUND);
    }

    @GetMapping("/{name}")
    public ResponseEntity<GiftCertificate> getCertificateByName(@PathVariable String name){
        log.info("Find certificate by name: {}", name);

        return new ResponseEntity<>(service.getByName(name), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<GiftCertificate> createCertificate(@RequestParam("tag") Set<String> tags,
                                             @RequestBody @Valid GiftCertificate certificate)
    {
        log.info("Info certificate - name: {}, price: {}, duration: {} ",
                certificate.getName(), certificate.getPrice(), certificate.getDuration());

        return new ResponseEntity<>(service.create(certificate,tags), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GiftCertificate> updateCertificate(GiftCertificate certificate){
        log.info("Info certificate - name: {}, price: {}, duration: {} ",
                certificate.getName(), certificate.getPrice(), certificate.getDuration());

        return ResponseEntity.ok(service.update(certificate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificate> deleteCertificate(@PathVariable Long id) {
        log.info("Delete certificate by id: {}", id);

        return ResponseEntity.ok(service.deleteById(id));
    }

}
