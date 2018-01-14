package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.atm.Note;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.util.Map;

public class AdminInfoAction implements Action {

    private Machine machine;

    public AdminInfoAction(Machine machine) {
        this.machine = machine;
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("ATM Info");
        terminal.writeln(String.format("Amount: %d", machine.getAmount()));
        Map<Note, Integer> cells = machine.getCellsInfo();
        if (cells.size() == 0) {
            return;
        }
        terminal.writeln("Cells:");
        for (Map.Entry<Note, Integer> entry : cells.entrySet()) {
            Note note = entry.getKey();
            terminal.writeln(String.format("%,8d - %d", note.getAmount(), entry.getValue()));
        }
    }

    @Override
    public String help() {
        return "Shows short info about ATM";
    }

    @Override
    public String formula() {
        return "info";
    }
}
