package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.util.List;

public class AdminHistoryAction implements Action {

    private Machine machine;

    public AdminHistoryAction(Machine machine) {
        this.machine = machine;
    }

    @Override
    public void execute(Terminal terminal) {
        List<String> history = machine.history();
        if (history.size() == 0) {
            terminal.writeln("There is no history yet");
        }
        int i = 1;
        for (String str : history) {
            terminal.writeln(String.format("%d. %s", i++, str));
        }
    }

    @Override
    public String help() {
        return "Shows history about ATM operations";
    }

    @Override
    public String formula() {
        return "history";
    }
}
