package ru.otus.sokolovsky.hw16.integration.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMessage implements Message {
    private String destination;
    private String source;
    private String name;
    private MessageType type;
    private Map<String, String> headers = new HashMap<>();

    AbstractMessage(String destination, String name, MessageType type) {
        this.destination = destination;
        this.name = name;
        this.type = type;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public MessageType getType() {
        return type;
    }
}
