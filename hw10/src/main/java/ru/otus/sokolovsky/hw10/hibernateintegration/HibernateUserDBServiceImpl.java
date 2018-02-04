package ru.otus.sokolovsky.hw10.hibernateintegration;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import ru.otus.sokolovsky.hw10.domain.UserDBService;
import ru.otus.sokolovsky.hw10.domain.UserDataSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Function;

public class HibernateUserDBServiceImpl implements UserDBService {

    private final SessionFactory sessionFactory;

    public HibernateUserDBServiceImpl(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public List<UserDataSet> readByName(String name) {
        return putTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
            Root<UserDataSet> from = criteria.from(UserDataSet.class);
            criteria.where(builder.equal(from.get("name"), name));
            return session.createQuery(criteria).list();
        });
    }

    @Override
    public void save(UserDataSet dataSet) {
        putTransaction(session -> session.save(dataSet));
    }

    @Override
    public UserDataSet read(long id) {
        return putTransaction(session -> session.load(UserDataSet.class, id));
    }

    @Override
    public List<UserDataSet> readAll() {
        return putTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
            criteria.from(UserDataSet.class);
            return session.createQuery(criteria).list();
        });
    }

    @Override
    public void shutdown() {
        sessionFactory.close();
    }

    private <R> R putTransaction(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}
