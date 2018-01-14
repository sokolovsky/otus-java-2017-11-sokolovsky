package ru.otus.sokolovsky.hw7.atm;

import ru.otus.sokolovsky.hw7.events.Event;
import ru.otus.sokolovsky.hw7.events.Listener;

public class RefreshListener implements Listener{

    private Machine atm;
    private Machine.State takenState;

    public RefreshListener(Machine atm) {
        this.atm = atm;
        takeState();
    }

    public void takeState() {
        takenState = atm.createState();
    }

    @Override
    public void notify(Event e) {
        if (takenState == null) {
            return;
        }
        atm.setState(takenState);
    }
}
