package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private String tableName = "users";


    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() { // HIBERNATE автоматически создает таблицу и удаляет ее в конце
        sessionFactory = new Util().getSessionFactory();
        session = sessionFactory.openSession();
        session.createSQLQuery("CREATE TABLE IF NOT EXISTS " + tableName + "(id bigint auto_increment not null, " +
                "name varchar(40) null, " +
                "lastName varchar(40) null, " +
                "age tinyint null, " +
                "constraint users_pk primary key (id));"
        ).executeUpdate();
        session.close();
        sessionFactory.close();
    }

    @Override
    public void dropUsersTable() {
        sessionFactory = new Util().getSessionFactory();
        session = sessionFactory.openSession();
        session.createSQLQuery("DROP TABLE IF EXISTS " + tableName).executeUpdate();
        session.close();
        sessionFactory.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        sessionFactory = new Util().getSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        try {
            session.save(new User(name, lastName, age));// ADD NEW USER object(entity) to TABLE
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        sessionFactory = new Util().getSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        try {
            session.delete(session.load(User.class, id)); // SELECT USER by ID and DELETE
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        sessionFactory = new Util().getSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        try {
            Query query = session.createSQLQuery("SELECT * FROM " + tableName).addEntity(User.class);
            userList = query.list();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
            sessionFactory.close();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {

        sessionFactory = new Util().getSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        try {
            session.createSQLQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
            sessionFactory.close();
        }

    }
}
