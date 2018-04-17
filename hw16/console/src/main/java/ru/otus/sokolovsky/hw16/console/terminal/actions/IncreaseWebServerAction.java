package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class IncreaseWebServerAction extends EnvironmentControlAction {
    @Override
    public void execute(Terminal terminal) {
        getEnvironmentDispatcher().increaseWebService();
    }

    @Override
    public String help() {
        return "Increases web servers count";
    }

    @Override
    public String formula() {
        return "increase:web port:integer";
    }
}
