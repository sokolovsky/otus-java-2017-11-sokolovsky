package ru.otus.sokolovsky.hw13.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw13.myorm.SqlExecutor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class InitDbUsers implements ServletContextListener {

    @Autowired
    private SqlExecutor executor;

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
        String[] sqlStrings = {
                "INSERT INTO address SET street=\"пл Комсомольская\", id=1",
                "INSERT INTO address SET street=\"пл Комсомольская\", id=2",
                "INSERT INTO user SET name=\"Иван\", age=18, address_id=1, id=1",
                "INSERT INTO user SET name=\"Петр\", age=19, address_id=2, id=2",
                "INSERT INTO user SET name=\"Николай\", age=20, id=3",
                "INSERT INTO phone SET number=\"1234\", user_id=1",
                "INSERT INTO phone SET number=\"4321\", user_id=1",
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
