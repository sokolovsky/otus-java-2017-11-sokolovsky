package ru.otus.sokolovsky.hw11.main;

import ru.otus.sokolovsky.hw11.myorm.SqlExecutor;
import ru.otus.sokolovsky.hw11.myormintegration.UserDBServiceImpl;

import java.io.*;
import java.sql.*;
import java.util.*;

/**

 ДЗ-11: my cache engine

 Напишите свой cache engine с soft references.
 Добавьте кэширование в DBService из заданий myORM или Hibernate ORM

 */
public class App {

    private static App app = new App();

    private Properties dbProps = new Properties();

    private App() {
        try {
            dbProps.load(getFileInputStream("db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static App getApplication() {
        return app;
    }

    public void createDbTables(Connection connection) throws SQLException {
        SqlExecutor executor = createExecutor(connection);
        Arrays.stream(dbProps.getProperty("createTableFiles").split(",")).map(String::trim).forEach((file) -> {
            try {
                executor.execUpdate(getFileContent(file));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage() + " " + file);
            }
        });
    }

    public void dropDbTables(Connection connection) throws SQLException {
        SqlExecutor executor = createExecutor(connection);
        Arrays.stream(dbProps.getProperty("dropTableFiles").split(",")).map(String::trim).forEach((file) -> {
            try {
                executor.execUpdate(getFileContent(file));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private InputStream getFileInputStream(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = getClassLoader();
        return new FileInputStream(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
    }

    private String getFileContent(String fileName) {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClassLoader();
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

    public SqlExecutor createExecutor(Connection connection) throws SQLException {
        if (connection.isClosed()) {
            throw new RuntimeException("Connection must be opened");
        }
        return new SqlExecutor(connection);
    }

    public Connection createConnection() {
        try {
            Class<?> driverClass = getClassLoader().loadClass(dbProps.getProperty("driver"));
            Driver driver = (Driver) driverClass.getConstructor().newInstance();
            DriverManager.registerDriver(driver);

            return DriverManager.getConnection(
                    dbProps.getProperty("connection"),
                    dbProps.getProperty("user"),
                    dbProps.getProperty("pass")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserDBServiceImpl createUserJDBCService(Connection connection) {
        return new UserDBServiceImpl(connection);
    }

    private ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }
}
