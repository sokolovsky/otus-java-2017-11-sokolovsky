package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class IncreaseDbWorkerAction extends EnvironmentControlAction {
    @Override
    public void execute(Terminal terminal) {
        getEnvironmentDispatcher().increaseDbService();
    }

    @Override
    public String help() {
        return "Increases db services per one";
    }

    @Override
    public String formula() {
        return "increase:db";
    }
}
