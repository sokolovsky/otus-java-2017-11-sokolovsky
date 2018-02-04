package ru.otus.sokolovsky.hw10.main;

import ru.otus.sokolovsky.hw10.myorm.Executor;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

/**
 Создайте в базе таблицу с полями:
 • id bigint(20) NOT NULL auto_increment
 • name varchar(255)
 • age int(3)
 Создайте абстрактный класс DataSet. Поместите long id в DataSet.
 Добавьте класс UserDataSet (с полями, которые соответствуют таблице) унаследуйте его от DataSet.
 Напишите Executor, который сохраняет наследников DataSet в базу и читает их из базы по id и классу.
 <T extends DataSet> void save(T user){…}
 <T extends DataSet> T load(long id, Class<T> clazz){…}
 Проверьте его работу на UserDataSet
 */
public class App {
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static App app = new App();
    private static Connection connection;

    private App() {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection("jdbc:mysql://localhost/otus", DB_USER, DB_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static App getApplication() {
        return app;
    }

    private Connection getConnection() throws SQLException {
        return connection;
    }

    public void createUsesTable() throws SQLException {
        new Executor(getConnection())
            .execUpdate(getFileContent("create_users_table.sql"));
    }

    public void dropUsersTable() throws SQLException {
        new Executor(getConnection())
                .execUpdate(getFileContent("delete_users_table.sql"));
    }

    private String getFileContent(String fileName) {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public Executor createExecutor() throws SQLException {
        return new Executor(getConnection());
    }
}
