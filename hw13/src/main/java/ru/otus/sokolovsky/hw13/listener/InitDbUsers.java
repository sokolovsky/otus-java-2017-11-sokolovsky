package ru.otus.sokolovsky.hw13.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class InitDbUsers implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }
}
