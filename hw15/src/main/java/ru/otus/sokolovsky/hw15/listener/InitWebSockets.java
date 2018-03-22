package ru.otus.sokolovsky.hw15.listener;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw15.chat.WebSocketServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitWebSockets implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
//        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        new WebSocketServer(10001).start();
        System.out.println("\n\nWeb socket started listening\n\n");
    }
}
