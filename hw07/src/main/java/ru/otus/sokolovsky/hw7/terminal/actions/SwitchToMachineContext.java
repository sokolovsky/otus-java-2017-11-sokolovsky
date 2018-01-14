package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.terminal.Terminal;
import ru.otus.sokolovsky.hw7.terminal.contexts.MachineContext;

public class SwitchToMachineContext implements Action {
    private Machine[] machines;
    private Machine current;
    private int currentNumber;

    public SwitchToMachineContext(Machine[] machines) {
        this.machines = machines;
    }

    public void setNumber(Object number) {
        current = machines[(int) number - 1];
        currentNumber = (int) number;
    }

    @Override
    public void execute(Terminal terminal) {
        if (current == null) {
            throw new RuntimeException("Need to use machine");
        }
        if (currentNumber > machines.length || currentNumber < 1) {
            return;
        }
        try {
            MachineContext context = new MachineContext(terminal, current, currentNumber);
            context.run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String help() {
        return "Switching to concrete machine, e.g. machine 3";
    }

    @Override
    public String formula() {
        return "machine <Integer:number>";
    }
}
