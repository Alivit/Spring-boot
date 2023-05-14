package ru.clevertec.ecl.spring.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    /**
     * Это поле интерфейса описывающее поведение
     * сервис обработчика запросов sql
     * @see GiftCertificateRepository
     */
    private final GiftCertificateRepository repository;

    /**
     * Метод созданный для добавления данных в таблицу gift_certificate
     *
     * @param certificate содержит данные о подарочном сертификате
     * @param tags содержит список тэгов сертификата
     */
    @Override
    @Transactional
    public void create(GiftCertificate certificate, Set<String> tags) {
        try {
            repository.save(createCertificateWithTags(certificate, tags));
            log.info("Info certificate - name: {}, price: {}, duration: {} ",
                    certificate.getName(), certificate.getPrice(), certificate.getDuration());
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
     * @param offset содержит начало страницы
     * @param limit содержит конец страницы
     * @param sort лист с обьектами по которым надо сортировать
     *
     * @return страницу с полученными сертификатами из базы данных
     */
    @Override
    public Page<GiftCertificate> getAll(Integer offset, Integer limit, String sort) {
        String[] sorting = sort.split(",");

        Page<GiftCertificate> certificates = repository.findAll(
                PageRequest.of(offset,limit, sortSetting(sorting[0], sorting[1])));
        if(certificates.isEmpty()) throw new NotFoundException("Certificates not found");
        log.info("Certificates : {}", certificates);

        return certificates;
    }

    /**
     * Метод который настраивает сортировку по имени поля и по методу ASC/DESC
     *
     * @param sortField содержит имя поле
     * @param sortMethod содержит метод сортировки ASC/DESC
     *
     * @return объект сортировки
     */
    private Sort sortSetting(String sortField, String sortMethod){
        if("ASC".equals(sortMethod)) {
            return Sort.by(Sort.Direction.ASC, sortField);
        }
        else {
            return Sort.by(Sort.Direction.DESC, sortField);
        }
    }

    /**
     * Метод созданный для обновления данных в таблице gift_certificate
     *
     * @param certificate содержит данные о подарочном сертификате
     * @param tags     содержит список тэгов сертификата
     */
    @Override
    @Transactional
    public void update(GiftCertificate certificate, Set<String> tags) {
        try {
            GiftCertificate certificateOld = repository.getReferenceById(certificate.getId());
            repository.save(updateCertificateWithTags(certificateOld, certificate, tags));
            log.info("Info certificate - name: {}, price: {}, duration: {} ",
                    certificate.getName(), certificate.getPrice(), certificate.getDuration());
        }catch (Exception e){
            throw new ServerErrorException("Error with Update certificate: " + e);
        }
    }

    /**
     * Метод созданный для добновления сертификата со списком тегов
     *
     * @param certificate содержит данные о подарочном сертификате
     * @param tagsSet содержит список тэгов сертификата
     *
     * @return объект сертификата со списком тегов
     */
    private GiftCertificate updateCertificateWithTags(GiftCertificate certificateOld,
                                                      GiftCertificate certificate,
                                                      Set<String> tagsSet)
    {
        certificateOld.setName(certificate.getName());
        certificateOld.setPrice(certificate.getPrice());
        certificateOld.setDuration(certificate.getDuration());
        Set<Tag> tags = tagsSet.stream().
                map(name -> new Tag(0,name,null)).
                collect(Collectors.toSet());

        certificate.setTags(tags);

        return certificateOld;
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
    public GiftCertificate deleteById(Long id) {
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
    public GiftCertificate getById(Long id) {
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
    public GiftCertificate getByName(String name) {
        GiftCertificate certificate = repository.findByName(name);
        if(certificate == null) throw new NotFoundException("Certificate with name - " + name + " not found");
        log.info("Certificate with name " + name + ": {}", certificate);

        return certificate;
    }
}
