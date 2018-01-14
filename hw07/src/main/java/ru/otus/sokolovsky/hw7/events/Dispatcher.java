package ru.otus.sokolovsky.hw7.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Dispatcher {
    private static Dispatcher ourInstance = new Dispatcher();

    Map<Event, Set<Listener>> listeners = new HashMap<>();

    public static Dispatcher getInstance() {
        return ourInstance;
    }

    private Dispatcher() {
    }

    public Dispatcher subscribe(Event e, Listener listener) {
        listeners.putIfAbsent(e, new HashSet<>());

        listeners
            .get(e)
            .add(listener);

        return this;
    }

    public void trigger(Event e) {
        listeners.getOrDefault(e, new HashSet<>()).forEach((Listener listener) -> {
            listener.notify(e);
        });
    }
}
