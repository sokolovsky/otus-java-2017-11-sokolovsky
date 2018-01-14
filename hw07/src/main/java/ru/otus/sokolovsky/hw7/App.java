package ru.otus.sokolovsky.hw7;


import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.atm.Note;
import ru.otus.sokolovsky.hw7.atm.RefreshListener;
import ru.otus.sokolovsky.hw7.events.Dispatcher;
import ru.otus.sokolovsky.hw7.events.Event;
import ru.otus.sokolovsky.hw7.terminal.contexts.ApplicationContext;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

public class App {

    private static Random random = new Random(100);

    public static void main(String[] args) throws Exception {

        Terminal terminal = new Terminal(new PrintWriter(System.out), new InputStreamReader(System.in));

        terminal.writeln("ATM department is opening on ...");
        terminal.writeln("Type 'help' for showing available commands");

        Machine first = new Machine().setState(getRandomState());
        Machine second = new Machine().setState(getRandomState());
        Machine third = new Machine().setState(getRandomState());

        Machine[] machines = new Machine[]{
            first, second, third
        };

        Event encashmentEvent = new Event("encashment");
        Dispatcher.getInstance()
            .subscribe(encashmentEvent, new RefreshListener(first))
            .subscribe(encashmentEvent, new RefreshListener(second))
            .subscribe(encashmentEvent, new RefreshListener(third));

        new ApplicationContext(terminal, machines).run();
    }

    private static Machine.State getRandomState() {
        Machine machine = new Machine();
        for (Note note : Note.values()) {
            machine.putMoney(note, random.nextInt(100));
        }
        return machine.createState();
    }
}
