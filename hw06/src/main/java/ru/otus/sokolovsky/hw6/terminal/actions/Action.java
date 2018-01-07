package ru.otus.sokolovsky.hw6.terminal.actions;

import ru.otus.sokolovsky.hw6.terminal.Terminal;

public interface Action {
    void run(Terminal terminal);
    String help();
    String formula();
}
