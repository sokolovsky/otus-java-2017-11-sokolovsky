package ru.otus.sokolovsky.hw16.integration.message;

import java.util.Map;

public interface Message {
    String getDestination();

    void setSource(String source);

    String getSource();

    String getName();

    Map<String, String> getHeaders();

    void setHeader(String name, String value);

    byte[] getBody();

    MessageTypes getType();
}
