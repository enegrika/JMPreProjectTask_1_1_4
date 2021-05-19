package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.*;

import javax.imageio.spi.ServiceRegistry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.*;

public class Util {
    // реализуйте настройку соединения с БД

    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String MySQL_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private final String DataBase_URL = "jdbc:mysql://localhost:3306/JMDatabaseTask_1_1_3";
    private final String USERNAME = "root";
    private final String PASSWORD = "bangkok7";

    private Connection connection;
    private Properties properties;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public Util() {

    }


    ////////// HIBERNATE connection SessionFactory - NO XML with "Properties" Object and SessionFactory

    public SessionFactory getSessionFactory() {

        if (sessionFactory == null) {

            //1st SET all necessary PROPERTIES // same as in hibernate.cfg.xml
            properties = new Properties();
            properties.setProperty(Environment.DRIVER, DRIVER);
            properties.setProperty(Environment.DIALECT, MySQL_DIALECT);
            properties.setProperty(Environment.URL, DataBase_URL);
            properties.setProperty(Environment.USER, USERNAME);
            properties.setProperty(Environment.PASS, PASSWORD);
            properties.setProperty(Environment.SHOW_SQL, "true");
            properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS,"thread");
            properties.setProperty(Environment.AUTOCOMMIT,"false");
//            properties.setProperty(Environment.HBM2DDL_AUTO, "update");//// BETTER NOT USE!

            //2nd MAKE Config Object //
            configuration = new Configuration();
            configuration.setProperties(properties);

            //3rd ADD OUR CLASSES TO be ANNOTATED (ABLE to CREATE READ UPDATE and DELETE (CRUD) operations with TABLES)
            configuration.addAnnotatedClass(User.class); // NOW our MODEL CLASS - "USER" can be ANNOTATED

            //4th BUILD SESSION FACTORY to execute Queries

            sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build());

        }
        return sessionFactory;
    }



    ////////// JDBC connection to DB with "Connection" Object

    public Connection getConnection() throws SQLException {

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
