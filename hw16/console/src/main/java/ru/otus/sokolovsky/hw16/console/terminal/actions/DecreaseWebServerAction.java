package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class DecreaseWebServerAction extends EnvironmentControlAction {
    @Override
    public void execute(Terminal terminal) {
        getEnvironmentDispatcher().decreaseWebService();
    }

    @Override
    public String help() {
        return "Decreases web servers count";
    }

    @Override
    public String formula() {
        return "decrease:web";
    }
}
