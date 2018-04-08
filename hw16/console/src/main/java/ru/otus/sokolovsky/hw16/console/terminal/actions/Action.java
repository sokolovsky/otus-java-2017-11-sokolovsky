package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public interface Action {
    void execute(Terminal terminal);
    String help();
    String formula();
}
