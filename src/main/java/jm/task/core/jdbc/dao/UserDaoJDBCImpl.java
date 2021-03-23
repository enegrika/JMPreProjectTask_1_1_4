package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Util connection;
    private Statement stmt;
    private User user;
    private Savepoint savepoint = null;
    private ResultSet resultSet;
    private final String tableName = "users";
    private final String ID = "id";
    private final String NAME = "name";
    private final String LASTNAME = "lastName";
    private final String AGE = "age";


    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try {
            connection = new Util();
            savepoint = connection.setSavepoint();
            stmt = connection.getConnection().createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + "(id bigint auto_increment not null, " +
                    "name varchar(40) null, " +
                    "lastName varchar(40) null, " +
                    "age tinyint null, " +
                    "constraint users_pk primary key (id));"
            );
            connection.commit();
        } catch (SQLException e) {
            System.err.println("can not CREATE TABLE");
            e.printStackTrace();
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                System.err.println("can not rollback ");
                e1.printStackTrace();
            }
        } finally {
            try {
                stmt.close();
                connection.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        try {
            connection = new Util();
            savepoint = connection.setSavepoint();
            stmt = connection.getConnection().createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName + " ;");
            connection.commit();
        } catch (SQLException e) {
            System.err.println("can not DELETE TABLE");
            e.printStackTrace();
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                System.err.println("can not rollback ");
                e1.printStackTrace();
            } finally {
                try {
                    stmt.close();
                    connection.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            connection = new Util();
            savepoint = connection.setSavepoint();
            stmt = connection.getConnection().createStatement();
            stmt.executeUpdate("INSERT INTO " + tableName
                    + "( " + NAME + ","
                    + " " + LASTNAME + ","
                    + " " + AGE
                    + ") VALUES ( '" + name + "', '" + lastName + "', " + age + " );");
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            connection.commit();
        } catch (SQLException e) {
            System.err.println("can not SAVE USER");
            e.printStackTrace();
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                System.err.println("can not rollback ");
                e1.printStackTrace();
            } finally {
                try {
                    stmt.close();
                    connection.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void removeUserById(long id) {
        try {
            connection = new Util();
            savepoint = connection.setSavepoint();
            stmt = connection.getConnection().createStatement();
            stmt.executeUpdate(" DELETE FROM " + tableName + " where " + ID + " = " + id + " ;");
            connection.commit();
        } catch (SQLException e) {
            System.err.println("can not REMOVE USER");
            e.printStackTrace();
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    stmt.close();
                    connection.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            connection = new Util();
            savepoint = connection.setSavepoint();
            stmt = connection.getConnection().createStatement();
            resultSet = stmt.executeQuery("SELECT * FROM " + tableName + " ;");
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                userList.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            System.err.println("can not GET ALL USERS");
            e.printStackTrace();
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                    stmt.close();
                    connection.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return userList;
    }

    public void cleanUsersTable() {
        try {
            connection = new Util();
            savepoint = connection.setSavepoint();
            stmt = connection.getConnection().createStatement();
            stmt.executeUpdate("TRUNCATE TABLE " + tableName + " ;");
            connection.commit();
        } catch (SQLException e) {
            System.err.println("can not CLEAR TABLE");
            e.printStackTrace();
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    stmt.close();
                    connection.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
