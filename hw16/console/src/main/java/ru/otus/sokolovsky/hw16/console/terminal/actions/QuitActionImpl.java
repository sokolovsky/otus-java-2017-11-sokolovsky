package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class QuitActionImpl implements Action {
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
