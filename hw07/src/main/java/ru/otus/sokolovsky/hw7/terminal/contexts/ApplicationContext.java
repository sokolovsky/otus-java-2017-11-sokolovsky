package ru.otus.sokolovsky.hw7.terminal.contexts;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.actions.*;

public class ApplicationContext extends Context {

    private Machine[] machines;

    public ApplicationContext(Terminal terminal, Machine[] machines) throws Exception {
        super(terminal);
        this.machines = machines;
    }

    protected String prompt() {
        return "ATM Department";
    }

    @Override
    protected Action[] actions() {
        return new Action[] {
            new ShowsMachineInfo(machines),
            new EncashmentAction(),
            new SwitchToMachineContext(machines)
        };
    }
}
