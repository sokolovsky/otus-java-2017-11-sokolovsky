package ru.otus.sokolovsky.hw15.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.sokolovsky.hw15.inmemory.Accounts;
import ru.otus.sokolovsky.hw15.provider.UsersProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class InitAccounts implements ServletContextListener {

    @Autowired
    private Accounts accounts;

    @Autowired
    private UsersProvider usersProvider;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        initAccounts();
    }

    private void initAccounts() {
        usersProvider.getRecords().forEach((k, v) -> {
            accounts.add(k, v);
        });
    }
}
