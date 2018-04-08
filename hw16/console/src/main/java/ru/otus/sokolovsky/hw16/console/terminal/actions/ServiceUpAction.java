package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.runner.ApplicationRunner;
import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

import java.io.IOException;

public class ServiceUpAction implements Action {

    private ApplicationRunner msRunner;

    public ServiceUpAction(ApplicationRunner msRunner) {
        this.msRunner = msRunner;
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("Run process...");
        try {
            msRunner.start();
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
