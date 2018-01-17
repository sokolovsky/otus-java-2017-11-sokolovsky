package ru.otus.sokolovsky.hw7.atm;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MachineStateTest {

    @Test
    public void useFromState() {
        Machine machine = new Machine();

        machine.putMoney(Note.ONE_HUNDRED, 2);
        machine.putMoney(Note.HALF_OF_HUNDRED, 6);

        Machine.State state = machine.createState();
        machine = new Machine(state);

        assertEquals(500, machine.getAmount());
        assertEquals(2, machine.getCellsInfo().size());
    }

    @Test
    public void createState() {
        Machine machine = new Machine();

        machine.putMoney(Note.ONE_HUNDRED, 2);
        machine.putMoney(Note.HALF_OF_HUNDRED, 6);

        assertEquals(500, machine.getAmount());
        Machine.State state = machine.createState();
        assertEquals(2, state.getCells().size());
    }

    @Test
    public void refreshState() {
        Machine machine = new Machine();

        machine.putMoney(Note.ONE_HUNDRED, 2);
        machine.putMoney(Note.HALF_OF_HUNDRED, 6);

        Machine.State state = machine.createState();

        machine.putMoney(Note.ONE_HUNDRED, 10);
        machine.setState(state);

        assertEquals(500, machine.getAmount());
        assertEquals(2, machine.getCellsInfo().size());
    }
}
