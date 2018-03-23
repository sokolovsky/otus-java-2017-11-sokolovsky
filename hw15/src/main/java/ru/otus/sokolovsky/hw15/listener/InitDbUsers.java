package ru.otus.sokolovsky.hw15.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw15.myorm.SqlExecutor;
import ru.otus.sokolovsky.hw15.servlet.Utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class InitDbUsers implements ServletContextListener {

    @Autowired
    private SqlExecutor executor;

    static final String PASSWORD = "123456";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        if (hasUsers()) {
            return;
        }
        createUsers();
    }

    private boolean hasUsers() {
        boolean[] resContainer = {false};
        try {
            executor.execSelect("select count(id) from user where 1=1", (ResultSet resultSet) -> {
                try {
                    resContainer[0] = resultSet.getInt(1) > 0;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resContainer[0];
    }

    private void createUsers() {
        String hash = Utils.generateHash(PASSWORD);
        String[] sqlStrings = {
                String.format("INSERT INTO user SET login=\"ivan\", age=18, password=\"%s\"", hash),
                String.format("INSERT INTO user SET login=\"user\", age=19, password=\"%s\"", hash),
                String.format("INSERT INTO user SET login=\"guest\", age=20, password=\"%s\"", hash),
                String.format("INSERT INTO user SET login=\"admin\", age=20, password=\"%s\"", hash),
        };
        Arrays.stream(sqlStrings).forEach(sql -> {
            try {
                executor.execInsert(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }
}
