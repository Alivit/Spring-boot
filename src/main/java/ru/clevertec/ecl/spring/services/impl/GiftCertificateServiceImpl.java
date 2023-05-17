package ru.clevertec.ecl.spring.services.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.spring.entity.GiftCertificate;
import ru.clevertec.ecl.spring.entity.Tag;
import ru.clevertec.ecl.spring.exception.NotFoundException;
import ru.clevertec.ecl.spring.exception.ServerErrorException;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;
import ru.clevertec.ecl.spring.services.GiftCertificateService;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс сервис crud операций для работы с подарочными сертификатами
 */
@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository repository;

    /**
     * Метод созданный для добавления данных в таблицу gift_certificate
     *
     * @param certificate содержит данные о подарочном сертификате
     * @param tags содержит список тэгов сертификата
     */
    @Override
    @Transactional
    public GiftCertificate create(@Valid GiftCertificate certificate, Set<String> tags) {
        try {
            repository.save(createCertificateWithTags(certificate, tags));
            log.info("Info certificate - name: {}, price: {}, duration: {} ",
                    certificate.getName(), certificate.getPrice(), certificate.getDuration());

            return getById(certificate.getId());
        }catch (Exception e){
            throw new ServerErrorException("Error with Insert certificate: " + e);
        }
    }

    /**
     * Метод созданный для добавление списка тегов в объект класса сертификата
     *
     * @param certificate содержит данные о подарочном сертификате
     * @param tagsSet содержит список тэгов сертификата
     *
     * @return объект сертификата со списком тегов
     */
    private GiftCertificate createCertificateWithTags(GiftCertificate certificate, Set<String> tagsSet){
        Set<Tag> tags = tagsSet.stream().
                map(name -> new Tag(0,name,null)).
                collect(Collectors.toSet());
        certificate.setTags(tags);

        return certificate;
    }

    /**
     * Метод чтения данных из таблицы gift_certificate
     * offset - начало страницы, limit - конец страницы
     * и сортирующий страницу по имени поля и по методу ASC/DESC
     *
     * @param pageable содержит информацию о страницы
     * @return страницу с полученными сертификатами из базы данных
     */
    @Override
    public Page<GiftCertificate> getAll(Pageable pageable) {
        Page<GiftCertificate> certificates = repository.findAll(pageable);
        if(certificates.isEmpty()) throw new NotFoundException("Certificates not found");
        log.info("Certificates : {}", certificates);

        return certificates;
    }

    /**
     * Метод созданный для обновления данных в таблице gift_certificate
     *
     * @param certificate содержит данные о подарочном сертификате
     */
    @Override
    @Transactional
    public GiftCertificate update(@Valid GiftCertificate certificate) {
        try {
            GiftCertificate certificateOld = repository.getReferenceById(certificate.getId());
            repository.save(updateCertificateWithTags(certificateOld, certificate));
            log.info("Info certificate - name: {}, price: {}, duration: {} ",
                    certificate.getName(), certificate.getPrice(), certificate.getDuration());

            return getById(certificate.getId());
        }catch (Exception e){
            throw new ServerErrorException("Error with Update certificate: " + e);
        }
    }

    /**
     * Метод созданный для удаления данных из таблицы gift_certificate по id
     *
     * @param id содержит id сертификата
     *
     * @return сертификат со всей информацией
     */
    @Override
    @Transactional
    public GiftCertificate deleteById(@Positive Long id) {
        try {
            GiftCertificate certificate = getById(id);
            repository.delete(certificate);
            log.info("Certificate with id " + id + " was deleted");
            return certificate;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new ServerErrorException("Error with Delete certificate: " + e);
        }
    }

    /**
     * Метод созданный для нахождения данных из таблицы gift_certificate по id
     *
     * @param id содержит id подарочного сертификата
     *
     * @return объект сертификата со списком тегов
     */
    @Override
    public GiftCertificate getById(@Positive Long id) {
        GiftCertificate certificate = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Certificate with id - " + id + " not found"));
        log.info("Certificate with id " + id + ": {}", certificate);

        return certificate;
    }

    /**
     * Метод созданный для нахождения данных из таблицы gift_certificate по названию
     *
     * @param name содержит название подарочного сертификата
     *
     * @return объект сертификата со списком тегов
     */
    @Override
    public GiftCertificate getByName(@NotBlank String name) {
        GiftCertificate certificate = repository.findByName(name);
        if(certificate == null) throw new NotFoundException("Certificate with name - " + name + " not found");
        log.info("Certificate with name " + name + ": {}", certificate);

        return certificate;
    }

    /**
     * Метод созданный для добновления сертификата со списком тегов
     *
     * @param certificate содержит данные о подарочном сертификате
     *
     * @return объект сертификата со списком тегов
     */
    private GiftCertificate updateCertificateWithTags(GiftCertificate certificateOld,
                                                      GiftCertificate certificate)
    {
        certificateOld.setName(certificate.getName());
        certificateOld.setPrice(certificate.getPrice());
        certificateOld.setDuration(certificate.getDuration());
        certificateOld.setTags(certificate.getTags());

        return certificateOld;
    }
}
