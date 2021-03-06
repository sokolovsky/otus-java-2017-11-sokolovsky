package ru.otus.sokolovsky.hw7.terminal.contexts;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.actions.*;

public class AdminContext extends Context {

    private Machine machine;
    private int number;

    public AdminContext(Terminal terminal, Machine machine, int number) throws Exception {
        super(terminal);
        this.machine = machine;
        this.number = number;
    }

    @Override
    protected String prompt() {
        return String.format("Admin/ATM–%d", number);
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new AdminInfoAction(machine),
            new AdminHistoryAction(machine)
        };
    }
}
