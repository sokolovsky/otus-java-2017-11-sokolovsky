package ru.otus.sokolovsky.hw15.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw15.provider.DatabaseBuilder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class CreateDbTables implements ServletContextListener {

    @Autowired
    private DatabaseBuilder databaseBuilder;

    public void contextInitialized(ServletContextEvent sce) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        databaseBuilder.createTables();
    }
}
