package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.contexts.AdminContext;

public class AdminSupplyMachine implements Action {

    private Machine machine;
    private int number;

    public AdminSupplyMachine(Machine machine, int number) {
        this.machine = machine;
        this.number = number;
    }

    @Override
    public void execute(Terminal terminal) {
        try {
            AdminContext context = new AdminContext(terminal, machine, number);
            context.run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String help() {
        return "Switching to admin context for managing";
    }

    @Override
    public String formula() {
        return "admin";
    }
}
