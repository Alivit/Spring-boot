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
    public void add(GiftCertificate certificate, Set<String> tagsSet) {
        session = Hibernate.getSessionFactory();

        Set<Tag> tags = tagsSet.stream().
                map(name -> new Tag(0,name,null)).
                collect(Collectors.toSet());
        certificate.setTags(tags);
        session.beginTransaction();
        session.save(certificate);
        session.getTransaction().commit();

        session.close();
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
    public void update(GiftCertificate certificate, Set<String> tagsSet) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        session.update(certificate);
        session.getTransaction().commit();

        session.close();
    }

    @Override
    public void remove(GiftCertificate certificate) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        session.remove(certificate);
        session.getTransaction().commit();

        session.close();
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
}
