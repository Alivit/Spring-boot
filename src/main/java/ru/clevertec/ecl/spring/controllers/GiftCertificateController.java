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

import ru.clevertec.ecl.spring.entities.GiftCertificate;
import ru.clevertec.ecl.spring.exception.AppError;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;
import ru.clevertec.ecl.spring.repository.TagRepository;
import ru.clevertec.ecl.spring.services.GiftCertificateService;

import java.util.List;
import java.util.Set;

/**
 * Класс контроллер для обработки запросов о подарочном сертификате
 */
@Controller
@RequiredArgsConstructor
public class GiftCertificateController {

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов sql
     * @see GiftCertificateRepository
     */
    private final GiftCertificateRepository giftCertificateRepository = new GiftCertificateService();

    @GetMapping("/certificates")
    public ResponseEntity<?> certificatesInfo(){
        List<GiftCertificate> certificates = giftCertificateRepository.getAll();

        if(certificates.size() == 0){
            return new ResponseEntity<>(new AppError(HttpStatus.NO_CONTENT.value(),
                    "Gift certificates not founded"), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/certificates/{id}")
    public ResponseEntity<?> getCertificateWithId(@PathVariable Long id){
        GiftCertificate certificate = giftCertificateRepository.getById(id);

        if(certificate == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Gift certificate with id " + id + " nor found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @PostMapping("/certificates")
    public ResponseEntity<?> createCertificate(@RequestParam("tag") Set<String> tags, GiftCertificate certificate){
        if(giftCertificateRepository.add(certificate, tags) == 1){
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Gift certificate with id " + certificate.getName() + " nor created"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @PutMapping("/certificates")
    public ResponseEntity<?> updateCertificate(@RequestParam("tag") Set<String> tags, GiftCertificate certificate){
        if(giftCertificateRepository.update(certificate, tags) == 1){
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Gift certificate with id " + certificate.getName() + " nor updated"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }
    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateRepository.getById(id);

        if(certificate == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Gift certificate with id " + id + " nor found"), HttpStatus.NOT_FOUND);
        }

        if(giftCertificateRepository.remove(certificate) == 1){
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Gift certificate with id " + id + " nor deleted"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }


}
