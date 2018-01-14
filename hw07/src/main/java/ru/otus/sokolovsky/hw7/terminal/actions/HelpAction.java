package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.util.List;

public class HelpAction implements Action {

    private List<Action> actions;

    public HelpAction(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public void run(Terminal terminal) {
        terminal.writeln("Available commands:");
        for (Action action : actions) {
            writeString(terminal, action);
        }
    }

    private void writeString(Terminal terminal, Action action) {
        terminal.writeln(String.format("    %s - %s", action.formula(), action.help()));
    }

    @Override
    public String help() {
        return "Shows help which depends on particular context with actions";
    }

    @Override
    public String formula() {
        return "help";
    }
}
