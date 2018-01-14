package ru.otus.sokolovsky.hw7.events;

public class Event {
    private final String name;

    public Event (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Event && name.equals(((Event) obj).name);
    }
}
