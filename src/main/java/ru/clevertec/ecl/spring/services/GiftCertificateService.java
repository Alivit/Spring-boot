package ru.clevertec.ecl.spring.services;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.spring.config.Hibernate;
import ru.clevertec.ecl.spring.entities.GiftCertificate;
import ru.clevertec.ecl.spring.entities.Tag;
import ru.clevertec.ecl.spring.repository.GiftCertificateRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс сервис crud операций для работы с подарочными сертификатами
 */
@Service
public class GiftCertificateService implements GiftCertificateRepository {

    /**
     * Поле с классом хранящую сессию подключения к базе данных
     */
    private Session session;

    /**
     * Метод созданный для добавления данных в таблицу gift_certificate
     * @param certificate содержит данные о подарочном сертификате
     * @param tagsSet содержит список тэгов сертификата
     * @return 0 если всё прошло без ошибок, а 1 если возникла ошибка
     */
    @Override
    public int add(GiftCertificate certificate, Set<String> tagsSet) {
        try {
            session = Hibernate.getSessionFactory();

            session.beginTransaction();
            session.saveOrUpdate(createCertificateWithTags(certificate, tagsSet));
            session.getTransaction().commit();

            session.close();
        }catch (Exception e){
            return 1;
        }

        return 0;
    }

    /**
     * Метод созданный для добавление списка тегов в объект класса сертификата
     * @param certificate содержит данные о подарочном сертификате
     * @param tagsSet содержит список тэгов сертификата
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
     * @return лист с полученными данными из базы данных
     */
    @Override
    public List<GiftCertificate> getAll() {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<GiftCertificate> certificates = session.createQuery("from GiftCertificate ", GiftCertificate.class).list();
        session.getTransaction().commit();

        session.close();
        return certificates;
    }

    /**
     *  Метод созданный для обновления данных в таблице gift_certificate
     * @param certificate содержит данные о подарочном сертификате
     * @param tagsSet содержит список тэгов сертификата
     * @return 0 если всё прошло без ошибок, а 1 если возникла ошибка
     */
    @Override
    public int update(GiftCertificate certificate, Set<String> tagsSet) {
        try {
            session = Hibernate.getSessionFactory();

            session.beginTransaction();
            session.update(createCertificateWithTags(certificate, tagsSet));
            session.getTransaction().commit();

            session.close();
        }catch (Exception e){
            return 1;
        }

        return 0;
    }

    /**
     * Метод созданный для удаления данных из таблицы gift_certificate по id
     * @param certificate содержит данные о подарочном сертификате
     * @return 0 если всё прошло без ошибок, а 1 если возникла ошибка
     */
    @Override
    public int remove(GiftCertificate certificate) {
        try {
            session = Hibernate.getSessionFactory();

            session.beginTransaction();
            session.remove(certificate);
            session.getTransaction().commit();

            session.close();
        }catch (Exception e){
            return 1;
        }

        return 0;
    }

    /**
     * Метод созданный для нахождения данных из таблицы gift_certificate по id
     * @param id содержит id подарочного сертификата
     * @return объект сертификата со списком тегов
     */
    @Override
    public GiftCertificate getById(Long id) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        GiftCertificate certificate = session.get(GiftCertificate.class, id);
        session.getTransaction().commit();

        session.close();
        return certificate;
    }

    /**
     * Метод созданный для нахождения данных из таблицы gift_certificate по названию
     * @param name содержит название подарочного сертификата
     * @return объект сертификата со списком тегов
     */
    @Override
    public List<GiftCertificate> getByName(String name) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<GiftCertificate> certificates = session.createQuery("FROM GiftCertificate WHERE name LIKE '%" + name + "%'").list();
        session.getTransaction().commit();

        session.close();
        return certificates;
    }
}
