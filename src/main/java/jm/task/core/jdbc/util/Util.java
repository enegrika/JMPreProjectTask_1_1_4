package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.*;

public class Util {
    // реализуйте настройку соединения с БД

    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String MySQL_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private final String DataBase_URL = "jdbc:mysql://localhost:3306/mysql_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "UserAccount@1975";

    private Connection connection;
    private Properties properties;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public Util(){

    }

    //////// HIBERNATE sessionFactory getter

    public SessionFactory getSessionFactory() {
        ////////// HIBERNATE connection - NO XML with "Properties" Object and SessionFactory

        //1st SET all necessary PROPERTIES
        properties = new Properties();
        properties.setProperty(Environment.DRIVER, DRIVER);
        properties.setProperty(Environment.DIALECT, MySQL_DIALECT);
        properties.setProperty(Environment.URL, DataBase_URL);
        properties.setProperty(Environment.USER, USERNAME);
        properties.setProperty(Environment.PASS, PASSWORD);

        properties.setProperty(Environment.HBM2DDL_AUTO, "update");//// VERY IMPORTANT SETTING

        //2nd MAKE Config Object
        configuration = new Configuration();
        configuration.setProperties(properties);

        //3rd ADD OUR CLASSES TO be ANNOTATED (ABLE to CREATE READ UPDATE and DELETE (CRUD) operations with TABLES)
        configuration.addAnnotatedClass(User.class); // NOW our MODEL CLASS - "USER" can be ANNOTATED

        //4th BUILD SESSION FACTORY to execute Queries
        sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().build());

        return sessionFactory;
    }

    ///////// JDBC getters and operations
    public Connection getConnection() throws SQLException {

        ////////// JDBC connection to DB with "Connection" Object

        connection = DriverManager.getConnection(DataBase_URL, USERNAME, PASSWORD);
        connection.setAutoCommit(false);// ОТКЛЮЧАЕМ! автовыполнение SQL запросов dlya rezhima TRANSACTIONS (НО ОТМЕНИТЬ МОЖНО ТОЛЬКО ОПЕРАЦИИ ИЗМЕНЕНИЯ данных в таблице!)
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);// уровень изоляции транзакции // выставляем уровень изоляции транзакции

        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(); // ОТМЕНА ИЗМЕНЕНИЙ в таблице и откат к предыдущему состоянию таблицы( СОХРАНЕННОМУ SavePoint)
    }

    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

}
