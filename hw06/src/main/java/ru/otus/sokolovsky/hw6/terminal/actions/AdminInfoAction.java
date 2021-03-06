package ru.otus.sokolovsky.hw6.terminal.actions;

import ru.otus.sokolovsky.hw6.atm.Machine;
import ru.otus.sokolovsky.hw6.atm.Note;
import ru.otus.sokolovsky.hw6.terminal.Terminal;

import java.util.Map;

public class AdminInfoAction implements Action {
    @Override
    public void run(Terminal terminal) {
        terminal.writeln("ATM Info");
        Machine machine = Machine.getInstance();
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
