package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;

public class Util {
    // реализуйте настройку соединения с БД

    private final String DataBase_URL = "jdbc:mysql://localhost:3306/mysql_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "UserAccount@1975";

    private Connection connection;

    public Util() throws SQLException {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("can not register driver");
//        }

        connection = DriverManager.getConnection(DataBase_URL, USERNAME, PASSWORD);
        connection.setAutoCommit(false);// ОТКЛЮЧАЕМ! автовыполнение SQL запросов - НО ОТМЕНИТЬ МОЖНО ТОЛЬКО ОПЕРАЦИИ ИЗМЕНЕНИЯ данных в таблице!
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);// уровень изоляции транзакции // выставляем уровень изоляции транзакции
//        System.out.println("Util connected to DB - SUCCESS ! ");
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback(); // ОТМЕНА ИЗМЕНЕНИЙ в таблице и откат к предыдущему состоянию таблицы( СОХРАНЕННОМУ SavePoint)
    }

    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }
}
