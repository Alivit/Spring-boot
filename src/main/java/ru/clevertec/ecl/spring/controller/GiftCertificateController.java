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
import ru.clevertec.ecl.spring.entity.Order;
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
    public ResponseEntity<Page<GiftCertificate>> getCertificates(Pageable pageable) {
        log.info("Request: get all certificates with Pageable: {}", pageable.toString());

        Page<GiftCertificate> response = service.getAll(pageable);
        log.info("Response: received number of certificates - {}", response.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> getCertificateById(@PathVariable Long id){
        log.info("Request: find certificate by id: {}", id);

        GiftCertificate response = service.getById(id);
        log.info("Response: found certificate: {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/{name}")
    public ResponseEntity<GiftCertificate> getCertificateByName(@PathVariable String name){
        log.info("Request: find certificate by name: {}", name);

        GiftCertificate response = service.getByName(name);
        log.info("Response: found certificate: {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<GiftCertificate> createCertificate(@RequestParam("tag") Set<String> tags, GiftCertificate certificate) {
        log.info("Request: info certificate: {}", certificate.toString());

        GiftCertificate response = service.create(certificate, tags);
        log.info("Response: created certificate: {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GiftCertificate> updateCertificate(GiftCertificate certificate){
        log.info("Request: info certificate: {}", certificate.toString());

        GiftCertificate response = service.update(certificate);
        log.info("Response: updated certificate: {}", response.toString());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificate> deleteCertificate(@PathVariable Long id) {
        log.info("Request: delete certificate by id: {}", id);

        GiftCertificate response = service.deleteById(id);
        log.info("Response: deleted certificate: {}", response.toString());

        return ResponseEntity.ok(response);
    }

}
