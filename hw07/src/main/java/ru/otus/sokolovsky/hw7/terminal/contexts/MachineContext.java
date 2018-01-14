package ru.otus.sokolovsky.hw7.terminal.contexts;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.actions.*;

public class MachineContext extends Context {

    private Machine machine;
    private int number;

    public MachineContext(Terminal terminal, Machine machine, int number) throws Exception {
        super(terminal);
        this.machine = machine;
        this.number = number;
    }

    @Override
    protected String prompt() {
        return String.format("ATM–%d", number);
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new GoToUseAccount(machine),
            new AdminSupplyMachine(machine, number)
        };
    }
}
