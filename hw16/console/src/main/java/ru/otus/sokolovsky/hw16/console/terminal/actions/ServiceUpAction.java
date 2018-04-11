package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.ms.MessageSystemManager;
import ru.otus.sokolovsky.hw16.console.runner.ApplicationRunner;
import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

import java.io.IOException;
import java.util.concurrent.Executors;

public class ServiceUpAction implements Action {

    private ApplicationRunner msRunner;
    private ApplicationRunner webRunner;
    private ApplicationRunner dbRunner;
    private MessageSystemManager msManager;

    public ServiceUpAction(ApplicationRunner msRunner, MessageSystemManager msManager) {
        this.msRunner = msRunner;
        this.msManager = msManager;
    }

    public void setWebRunner(ApplicationRunner webRunner) {
        this.webRunner = webRunner;
    }

    public void setDbRunner(ApplicationRunner dbRunner) {
        this.dbRunner = dbRunner;
    }

    @Override
    public void execute(Terminal terminal) {
        try {
            terminal.writeln("Run Message System process...");
            msRunner.start();

            String systemWasStartedMessage = "Message System was started";

            // run ms system
            msRunner.afterStart(systemWasStartedMessage, () -> Executors.newSingleThreadExecutor().submit(msManager));

            // setting up the new channel of web service
            msRunner.afterStart(systemWasStartedMessage, () -> msManager.createChannel("CHAT_BROADCAST"));

            // setting up the new channel of Database service
            msRunner.afterStart(systemWasStartedMessage, () -> msManager.createChannel("DB"));

            // run db system
            if (webRunner != null) {
                msRunner.afterStart(systemWasStartedMessage, () -> {
                    try {
                        System.out.println("DB system starting");
                        webRunner.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            if (dbRunner != null) {
                msRunner.afterStart(systemWasStartedMessage, () -> {
                    try {
                        System.out.println("Web system starting");
                        dbRunner.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "Starts main system processes";
    }

    @Override
    public String formula() {
        return "start";
    }
}
