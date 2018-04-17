package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class DecreaseDbWorkerAction extends EnvironmentControlAction {

    @Override
    public void execute(Terminal terminal) {
        getEnvironmentDispatcher().decreaseDbService();
    }

    @Override
    public String help() {
        return "Decreases db services per one";
    }

    @Override
    public String formula() {
        return "decrease:db";
    }
}
