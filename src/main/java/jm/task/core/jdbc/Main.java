package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        userService.saveUser("Levy","Drun", (byte) 35);
        userService.saveUser("Cozy","Bled", (byte) 37);
        userService.saveUser("John","Corn", (byte) 39);
        userService.saveUser("Ben","Faedy", (byte) 40);

        userService.removeUserById(2);

        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
//        userService.dropUsersTable();
    }
}
