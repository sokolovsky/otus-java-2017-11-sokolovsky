package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class IncreaseWebServerAction implements Action {
    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("Web instance was increased with port");
    }

    @Override
    public String help() {
        return "Increases web servers count with specified port";
    }

    @Override
    public String formula() {
        return "increase:web port:integer";
    }
}
