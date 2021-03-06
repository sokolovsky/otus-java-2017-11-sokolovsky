package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.terminal.Terminal;

public class QuitAction implements Action {
    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("Quit");
    }

    @Override
    public String help() {
        return "Quits from current context";
    }

    @Override
    public String formula() {
        return "quit";
    }
}
