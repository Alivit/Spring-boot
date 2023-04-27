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

@Service
public class TagService implements TagRepository {

    private Session session;

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

    @Override
    public List<Tag> getAll() {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<Tag> tags = session.createQuery("from Tag ", Tag.class).getResultList();
        session.getTransaction().commit();

        session.close();
        return tags;
    }

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

    @Override
    public Tag getById(Long id) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        Tag tag = session.get(Tag.class, id);
        session.getTransaction().commit();

        session.close();
        return tag;
    }

    public Tag getById(Long id, String sort, List<String> sortBy) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        Tag tag = session.get(Tag.class, id);
        session.getTransaction().commit();
        sortedCertificates(tag, sort, sortBy);

        session.close();
        return tag;
    }

    @Override
    public List<Tag> findByName(String name) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<Tag> tags = session.createQuery("FROM Tag WHERE name LIKE '%" + name + "%'").list();
        session.getTransaction().commit();

        session.close();
        return tags;
    }

    public List<Tag> findByName(String name, String sort, List<String> sortBy) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<Tag> tags = session.createQuery("FROM Tag WHERE name LIKE '%" + name + "%'").list();
        session.getTransaction().commit();
        tags.forEach(tag ->sortedCertificates(tag, sort, sortBy));

        session.close();
        return tags;
    }

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
