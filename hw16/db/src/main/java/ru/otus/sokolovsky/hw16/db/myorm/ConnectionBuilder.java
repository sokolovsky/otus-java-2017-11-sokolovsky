package ru.otus.sokolovsky.hw16.db.myorm;

import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class ConnectionBuilder {
    @Bean
    public static Connection build(String connStr, String driverDefinition, String user, String pass) {
        try {
            Class<?> driverClass = getClassLoader().loadClass(driverDefinition);
            Driver driver = (Driver) driverClass.getConstructor().newInstance();
            DriverManager.registerDriver(driver);

            return DriverManager.getConnection(connStr, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static ClassLoader getClassLoader() {
        return ConnectionBuilder.class.getClassLoader();
    }
}
