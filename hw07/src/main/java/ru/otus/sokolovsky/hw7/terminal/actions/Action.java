package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.terminal.Terminal;

public interface Action {
    void execute(Terminal terminal);
    String help();
    String formula();
}
