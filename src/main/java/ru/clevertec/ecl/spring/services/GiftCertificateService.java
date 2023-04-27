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

@Service
public class GiftCertificateService implements GiftCertificateRepository {

    private Session session;

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

    private GiftCertificate createCertificateWithTags(GiftCertificate certificate, Set<String> tagsSet){
        Set<Tag> tags = tagsSet.stream().
                map(name -> new Tag(0,name,null)).
                collect(Collectors.toSet());
        certificate.setTags(tags);

        return certificate;
    }

    @Override
    public List<GiftCertificate> getAll() {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<GiftCertificate> certificates = session.createQuery("from GiftCertificate ", GiftCertificate.class).list();
        session.getTransaction().commit();

        session.close();
        return certificates;
    }

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

    @Override
    public GiftCertificate getById(Long id) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        GiftCertificate certificate = session.get(GiftCertificate.class, id);
        session.getTransaction().commit();

        session.close();
        return certificate;
    }

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
