package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

import java.util.Map;

public class ServiceUpAction implements Action {

    public ServiceUpAction() {
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("....Run process");
    }

    @Override
    public String help() {
        return "Starts main system processes";
    }

    @Override
    public String formula() {
        return "start";
    }
}
