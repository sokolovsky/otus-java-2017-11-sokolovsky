package ru.otus.sokolovsky.hw16.console.terminal.actions;

import ru.otus.sokolovsky.hw16.console.terminal.Terminal;

public class EnvironmentQuitAction extends EnvironmentControlAction implements QuitAction {
    @Override
    public void execute(Terminal terminal) {
        getEnvironmentDispatcher().shutdown();
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
