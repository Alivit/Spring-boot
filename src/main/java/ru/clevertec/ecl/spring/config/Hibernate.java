package ru.clevertec.ecl.spring.config;

import jakarta.persistence.PersistenceUnit;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Класс отвечающий за подключение к базе данных
 * c помощью hibernate
 */
public class Hibernate {

    /**
     * Поле с классом хранящую сессию подключения к базе данных
     */
    @PersistenceUnit(name = "factory")
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
        catch(Throwable th){
            System.err.println("Enitial SessionFactory creation failed"+th);
            throw new ExceptionInInitializerError(th);
        }
    }

    public static Session getSessionFactory() throws HibernateException {
        return sessionFactory.openSession();
    }

}
