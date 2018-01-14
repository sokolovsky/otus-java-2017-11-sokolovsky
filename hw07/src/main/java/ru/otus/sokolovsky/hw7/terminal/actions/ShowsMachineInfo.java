package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

public class ShowsMachineInfo implements Action {

    private Machine[] machines;

    public ShowsMachineInfo(Machine[] machines) {
        this.machines = machines;
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.writeln(String.format("Info of `%d` machines: ", machines.length));

        int sum = 0;
        int n = 1;
        for (Machine machine : machines) {
            terminal.writeln(String.format("Number %d, sum - %d", n, machine.getAmount()));
            n++;
            sum += machine.getAmount();
        }
        terminal.writeln(String.format("Total: %d", sum));
    }

    @Override
    public String help() {
        return "Shows info about machines in department";
    }

    @Override
    public String formula() {
        return "info";
    }
}
