package ru.otus.sokolovsky.hw6.atm;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Map;

import static org.junit.Assert.*;

public class MachineTest {

    @SuppressWarnings("unchecked")
    private Machine newMachine() throws Exception {
        try {
            Constructor<Machine> constructor = null;
            constructor = (Constructor<Machine>) Machine.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void putMoney() throws Exception {
        Machine machine = newMachine();
        machine.putMoney(Note.FIVE_THOUSAND, 2); // 10 000
        machine.putMoney(Note.COUPLE_OF_HUNDRED, 4); // 10 800

        assertEquals(10_800, machine.getAmount());
    }

    @Test
    public void canGetSum() throws Exception {
        Machine machine = newMachine();
        assertEquals(0, machine.getAmount());

        machine.putMoney(Note.COUPLE_OF_HUNDRED, 4);
        assertTrue(machine.canGetSum(600));
        assertFalse(machine.canGetSum(500));
    }

    @Test
    public void getMoney() throws Exception {
        Machine machine = newMachine();
        machine.putMoney(Note.ONE_THOUSAND, 5);
        machine.putMoney(Note.FIVE_THOUSAND, 2);
        machine.putMoney(Note.ONE_HUNDRED, 10);
        machine.putMoney(Note.HALF_OF_HUNDRED, 2);

        Map<Note, Integer> money = machine.getMoney(10_500);
        assertEquals(2, (int) money.get(Note.FIVE_THOUSAND));
        assertEquals(5, (int) money.get(Note.ONE_HUNDRED));
    }

    @Test
    public void history() throws Exception {
        Machine machine = newMachine();
        machine.putMoney(Note.ONE_THOUSAND, 5);
        machine.putMoney(Note.FIVE_THOUSAND, 2);
        machine.putMoney(Note.ONE_HUNDRED, 10);
        machine.putMoney(Note.HALF_OF_HUNDRED, 2);

        machine.getMoney(1000);

        assertEquals(5, machine.history().size());
    }

    @Test
    public void amountAfterWithdraw() throws Exception {
        Machine machine = newMachine();
        machine.putMoney(Note.ONE_HUNDRED, 10);
        machine.putMoney(Note.HALF_OF_HUNDRED, 2); // 1 100

        machine.getMoney(1000); // 100

        assertEquals(100, machine.getAmount());
    }

    @Test
    public void noteCountAfterTaking() throws Exception {
        Machine machine = newMachine();
        machine.putMoney(Note.ONE_HUNDRED, 10);
        machine.putMoney(Note.HALF_OF_HUNDRED, 2); // 1 100

        machine.getMoney(200);
        assertEquals(8, (int) machine.getCellsInfo().get(Note.ONE_HUNDRED));
    }
}