package ru.clevertec.ecl.spring.services;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.spring.config.Hibernate;
import ru.clevertec.ecl.spring.entities.GiftCertificate;
import ru.clevertec.ecl.spring.entities.Tag;
import ru.clevertec.ecl.spring.repository.TagRepository;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс сервис crud операций для работы с тэгами
 */
@Service
public class TagService implements TagRepository {

    /**
     * Поле с классом хранящую сессию подключения к базе данных
     */
    private Session session;

    /**
     * Метод созданный для добавления данных в таблицу tag
     *
     * @param names содержит список названий подарочных сертификатов
     * @param prices содержит список цен подарочных сертификатов
     * @param durations содержит список продолжительноти подарочных сертификатов
     * @param tag содержит название тега
     * @return 0 если всё прошло без ошибок, а 1 если возникла ошибка
     */
    @Override
    public int add(List<String> names, List<Double> prices, List<Integer> durations, String tag) {
        try {
            session = Hibernate.getSessionFactory();

            session.beginTransaction();
            session.saveOrUpdate(createTagWithCertificates(names, prices, durations, tag));
            session.getTransaction().commit();

            session.close();
        }catch (Exception e){
            return 1;
        }
        return 0;
    }

    /**
     * Метод созданный для добавление списка сертификатов в объект класса тега
     *
     * @param names содержит список названий подарочных сертификатов
     * @param prices содержит список цен подарочных сертификатов
     * @param durations содержит список продолжительноти подарочных сертификатов
     * @param tagName содержит название тега
     * @return объект тега со списком сертификатов
     */
    private Tag createTagWithCertificates(List<String> names, List<Double> prices,
                                          List<Integer> durations, String tagName){
        Tag tag = new Tag();
        tag.setName(tagName);

        IntStream.range(0, names.size()).forEach(i -> {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setName(names.get(i));
            certificate.setPrice(prices.get(i));
            certificate.setDuration(durations.get(i));
            tag.getCertificate().add(certificate);
        });

        return tag;
    }

    /**
     * Метод чтения данных из таблицы tag
     *
     * @return лист с полученными данными из базы данных
     */
    @Override
    public List<Tag> getAll() {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<Tag> tags = session.createQuery("from Tag ", Tag.class).getResultList();
        session.getTransaction().commit();

        session.close();
        return tags;
    }

    /**
     * Метод созданный для обновления данных в таблице tag
     *
     * @param names содержит список названий подарочных сертификатов
     * @param prices содержит список цен подарочных сертификатов
     * @param durations содержит список продолжительноти подарочных сертификатов
     * @param tag содержит название тега
     * @return 0 если всё прошло без ошибок, а 1 если возникла ошибка
     */
    @Override
    public int update(List<String> names, List<Double> prices,
                      List<Integer> durations, String tag) {
        try {
            session = Hibernate.getSessionFactory();

            session.beginTransaction();
            session.update(createTagWithCertificates(names, prices, durations, tag));
            session.getTransaction().commit();

            session.close();
        } catch (Exception e){
            return 1;
        }
        return 0;
    }

    /**
     * Метод созданный для удаления данных из таблицы tag по id
     *
     * @param tag содержит данные о теге
     * @return 0 если всё прошло без ошибок, а 1 если возникла ошибка
     */
    @Override
    public int remove(Tag tag) {
        try {
            session = Hibernate.getSessionFactory();

            session.beginTransaction();
            session.remove(tag);
            session.getTransaction().commit();

            session.close();
        } catch (Exception e){
            return 1;
        }
        return 0;
    }

    /**
     * Метод созданный для нахождения данных из таблицы tag по id
     *
     * @param id содержит id тега
     * @return объект тега со списком сертефикатов
     */
    @Override
    public Tag getById(Long id) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        Tag tag = session.get(Tag.class, id);
        session.getTransaction().commit();

        session.close();
        return tag;
    }

    /**
     * Переопределённый метод созданный для нахождения данных из таблицы tag по id
     * и сортирующий по имени и времени по ASC/DESC
     *
     * @param id содержит id тега
     * @param sort содержит тип сотрировки
     * @param sortBy лист с обьектами по которым надо сортировать
     * @return объект тега со списком сертефикатов
     */
    public Tag getById(Long id, String sort, List<String> sortBy) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        Tag tag = session.get(Tag.class, id);
        session.getTransaction().commit();
        sortedCertificates(tag, sort, sortBy);

        session.close();
        return tag;
    }

    /**
     * Метод созданный для нахождения данных из таблицы tag по названию
     *
     * @param name содержит название тега
     * @return объект тега со списком сертефикатов
     */
    @Override
    public List<Tag> findByName(String name) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<Tag> tags = session.createQuery("FROM Tag WHERE name LIKE '%" + name + "%'").list();
        session.getTransaction().commit();

        session.close();
        return tags;
    }

    /**
     * Переопределённый метод созданный для нахождения данных из таблицы tag по названию
     * и сортирующий по имени и времени по ASC/DESC
     *
     * @param name содержит название тега
     * @param sort содержит тип сотрировки
     * @param sortBy лист с обьектами по которым надо сортировать
     * @return объект тега со списком сертефикатов
     */
    public List<Tag> findByName(String name, String sort, List<String> sortBy) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<Tag> tags = session.createQuery("FROM Tag WHERE name LIKE '%" + name + "%'").list();
        session.getTransaction().commit();
        tags.forEach(tag ->sortedCertificates(tag, sort, sortBy));

        session.close();
        return tags;
    }

    /**
     * Метод созданный для сортировки данных по имени и времени по ASC/DESC
     *
     * @param tag содержит данные о теге
     * @param sort содержит тип сотрировки
     * @param sortBy лист с обьектами по которым надо сортировать
     */
    private void sortedCertificates(Tag tag, String sort, List<String> sortBy){
        if("ASC".equals(sort)) {
            if(sortBy.stream().anyMatch("name"::equals)) {
                tag.setCertificate(tag.getCertificate()
                        .stream()
                        .sorted(Comparator.comparing(GiftCertificate::getName))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }else if (sortBy.stream().anyMatch("time"::equals)){
                tag.setCertificate(tag.getCertificate()
                        .stream()
                        .sorted(Comparator.comparing(GiftCertificate::getCreate_date))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            } else {
                tag.setCertificate(tag.getCertificate()
                        .stream()
                        .sorted(Comparator.comparing(GiftCertificate::getCreate_date)
                                .thenComparing(GiftCertificate::getName))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
        }else {
            if(sortBy.stream().anyMatch("name"::equals)) {
                tag.setCertificate(tag.getCertificate()
                        .stream()
                        .sorted(Comparator.comparing(GiftCertificate::getName).reversed())
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }else if (sortBy.stream().anyMatch("time"::equals)){
                tag.setCertificate(tag.getCertificate()
                        .stream()
                        .sorted(Comparator.comparing(GiftCertificate::getCreate_date).reversed())
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            } else {
                tag.setCertificate(tag.getCertificate()
                        .stream()
                        .sorted(Comparator.comparing(GiftCertificate::getCreate_date).reversed()
                                .thenComparing(GiftCertificate::getName).reversed())
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
        }
    }

}
