package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.environment.EnvironmentDispatcher;
import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class ServiceUpAction implements Action {

    private EnvironmentDispatcher envDispatcher;

    public ServiceUpAction(EnvironmentDispatcher envDispatcher) {
        this.envDispatcher = envDispatcher;
    }

    @Override
    public void execute(Terminal terminal) {
        envDispatcher.runEnvironment();
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
