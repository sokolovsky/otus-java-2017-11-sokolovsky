package ru.otus.sokolovsky.hw6.terminal.actions;

import ru.otus.sokolovsky.hw6.terminal.Terminal;

public class QuitAction implements Action {
    @Override
    public void run(Terminal terminal) {
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
