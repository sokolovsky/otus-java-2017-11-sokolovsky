package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class IncreaseDbWorkerAction implements Action {
    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("Increased services, now are - 10");
    }

    @Override
    public String help() {
        return "Increases db services by one";
    }

    @Override
    public String formula() {
        return "increase:db";
    }
}
