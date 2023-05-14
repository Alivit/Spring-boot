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

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов сертификатов
     * @see GiftCertificateService
     */
    private final GiftCertificateService service;

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public Page<GiftCertificate> getCertificates(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                                 @RequestParam(value = "limit", defaultValue = "10") @Min(1) Integer limit,
                                                 @RequestParam(value = "sort", defaultValue = "id,ASC")@NotEmpty String sort)
    {
        log.info("Get all certificates with offset: {}, limit: {}, sort: {}", offset, limit, sort);

        return service.getAll(offset, limit, sort);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public GiftCertificate getCertificateById(@PathVariable Long id){
        log.info("Find certificate by id: {}", id);

        return service.getById(id);
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.FOUND)
    public GiftCertificate getCertificateByName(@PathVariable("name") String name){
        log.info("Find certificate by name: {}", name);

        return service.getByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate createCertificate(@RequestParam("tag") Set<String> tags,
                                             @RequestBody @Valid GiftCertificate certificate)
    {
        service.create(certificate,tags);
        log.info("Info certificate - name: {}, price: {}, duration: {} ",
                certificate.getName(), certificate.getPrice(), certificate.getDuration());

        return certificate;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate updateCertificate(@RequestParam("tag") Set<String> tags, GiftCertificate certificate){
        service.update(certificate,tags);
        log.info("Info certificate - name: {}, price: {}, duration: {} ",
                certificate.getName(), certificate.getPrice(), certificate.getDuration());

        return certificate;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate deleteCertificate(@PathVariable Long id) {
        log.info("Delete certificate by id: {}", id);

        return service.deleteById(id);
    }

}
