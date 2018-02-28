package ru.otus.sokolovsky.hw13.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitAccounts implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Init account event ======");
        System.out.println(sce);
    }
}
