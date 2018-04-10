package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.ms.MessageSystemManager;
import ru.otus.sokolovsky.hw16.console.runner.ApplicationRunner;
import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

import java.io.IOException;

public class ServiceUpAction implements Action {

    private ApplicationRunner msRunner;
    private MessageSystemManager msManager;

    public ServiceUpAction(ApplicationRunner msRunner, MessageSystemManager msManager) {
        this.msRunner = msRunner;
        this.msManager = msManager;
    }

    @Override
    public void execute(Terminal terminal) {
        try {
            terminal.writeln("Run Message System process...");
            msRunner.start();
            // setting up the new channel of Database service
            msRunner.afterStart("Message System was started", () -> msManager.createChannel("DB"));
            // setting up the new channel of web service
            msRunner.afterStart("Message System was started", () -> msManager.createChannel("CHAT_BROADCAST"));
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
