package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class ServiceUpAction extends EnvironmentControlAction {
    @Override
    public void execute(Terminal terminal) {
        getEnvironmentDispatcher().runEnvironment();
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
