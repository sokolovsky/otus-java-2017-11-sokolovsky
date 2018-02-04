package ru.otus.sokolovsky.hw10.main;

import org.hibernate.cfg.Configuration;
import ru.otus.sokolovsky.hw10.hibernateintegration.HibernateUserDBServiceImpl;
import ru.otus.sokolovsky.hw10.myorm.SqlExecutor;
import ru.otus.sokolovsky.hw10.myormintegration.UserDBServiceImpl;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

/**

 На основе ДЗ 9:
     1. Оформить решение в виде DBService (interface DBService, class DBServiceImpl, UsersDAO, UsersDataSet,
     SqlExecutor)
     2. Не меняя интерфейс DBSerivice сделать DBServiceHibernateImpl на Hibernate.
     3. Добавить в UsersDataSet поля:
         адресс (OneToOne)
         class AddressDataSet{
         private String street;
         }
         и телефон* (OneToMany)
         class PhoneDataSet{
         private String number;
         }
 Добавить соответствущие датасеты и DAO.
            можно не поддерживать в ДЗ 9
 */
public class App {

    private static App app = new App();
    private static Connection connection;
    private HibernateUserDBServiceImpl hibernateService;
    private UserDBServiceImpl jdbcService;

    private Properties dbProps = new Properties();
    private Properties hibernateProps = new Properties();

    private App() {
        initJDBCConnection();
        initHibernate();
    }

    public static App getApplication() {
        return app;
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }

    public void createDbTables() throws SQLException {
        SqlExecutor executor = createExecutor();
        Arrays.stream(dbProps.getProperty("createTableFiles").split(",")).map(String::trim).forEach((file) -> {
            try {
                executor.execUpdate(getFileContent(file));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void dropDbTables() throws SQLException {
        SqlExecutor executor = createExecutor();
        Arrays.stream(dbProps.getProperty("dropTableFiles").split(",")).map(String::trim).forEach((file) -> {
            try {
                executor.execUpdate(getFileContent(file));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public HibernateUserDBServiceImpl getHibernateService() {
        return hibernateService;
    }

    public UserDBServiceImpl getJdbcService() {
        return jdbcService;
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

    public SqlExecutor createExecutor() throws SQLException {
        return new SqlExecutor(getConnection());
    }

    private void initJDBCConnection() {
        try {
            dbProps.load(getFileInputStream("db.properties"));
            hibernateProps.load(getFileInputStream("hibernate.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Class<?> driverClass = getClassLoader().loadClass(dbProps.getProperty("driver"));
            Driver driver = (Driver) driverClass.getConstructor().newInstance();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(
                    dbProps.getProperty("connection"),
                    dbProps.getProperty("user"),
                    dbProps.getProperty("pass")
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        jdbcService = new UserDBServiceImpl(connection);

    }

    private ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    private void initHibernate() {
        Configuration configuration = new Configuration();

        Arrays.stream(hibernateProps.getProperty("domain.entities").split(","))
                .map(String::trim).forEach(
                        entity -> {
                            try {
                                configuration.addAnnotatedClass(Class.forName(entity));
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    );

        hibernateProps.entrySet().stream().filter(
                e -> {
                    String key = (String) e.getKey();
                    return key.contains("hibernate.");
                }
            ).forEach(e -> configuration.setProperty((String) e.getKey(), (String) e.getValue()));

        hibernateService = new HibernateUserDBServiceImpl(configuration);
    }

    private String getDbProperty(String name) {
        return dbProps.getProperty(name);
    }

    private Map<String, String> getHibernatePropertyList() {
        return null;
    }
}
