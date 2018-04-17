package ru.otus.sokolovsky.hw16.console.terminal;

import java.util.function.Consumer;

public class TerminalLogTunnel implements Consumer<String> {

    private Terminal terminal;

    TerminalLogTunnel(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void accept(String s) {
        terminal.logLine(s);
    }
}
