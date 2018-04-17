package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class IncreaseWebServerAction extends EnvironmentControlAction {

    private int port;

    public void setPort(Object port) {
        this.port = (Integer) port;
    }

    @Override
    public void execute(Terminal terminal) {
        getEnvironmentDispatcher().increaseWebService(port);
    }

    @Override
    public String help() {
        return "Increases web servers count";
    }

    @Override
    public String formula() {
        return "increase:web <integer:port>";
    }
}
