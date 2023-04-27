package ru.clevertec.ecl.spring.services;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.spring.config.Hibernate;
import ru.clevertec.ecl.spring.entities.Tag;
import ru.clevertec.ecl.spring.repository.TagRepository;

import java.util.List;

@Service
public class TagService implements TagRepository {

    private Session session;

    @Override
    public void add(Tag tag) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        session.save(tag);
        session.getTransaction().commit();

        session.close();
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
    public void update(Tag tag) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        session.update(tag);
        session.getTransaction().commit();

        session.close();
    }

    @Override
    public void remove(Tag tag) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        session.remove(tag);
        session.getTransaction().commit();

        session.close();
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

    @Override
    public List<Tag> findByName(String name) {
        session = Hibernate.getSessionFactory();

        session.beginTransaction();
        List<Tag> tags = session.createQuery("FROM Tag WHERE name LIKE '%" + name + "%'").list();
        session.getTransaction().commit();

        session.close();
        return tags;
    }
}
