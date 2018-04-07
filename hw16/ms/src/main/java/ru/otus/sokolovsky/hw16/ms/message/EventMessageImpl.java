package ru.otus.sokolovsky.hw16.ms.message;

import java.util.Map;

public class EventMessageImpl extends TextMessageImpl implements EventMessage {
    EventMessageImpl(String destination) {
        super(destination);
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
