package ru.otus.sokolovsky.hw16.db.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw15.chat.ChatServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitFrontend implements ServletContextListener {

    @Autowired
    private ChatServer chatServer;

    public void contextInitialized(ServletContextEvent sce) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        chatServer.start();
    }
}
