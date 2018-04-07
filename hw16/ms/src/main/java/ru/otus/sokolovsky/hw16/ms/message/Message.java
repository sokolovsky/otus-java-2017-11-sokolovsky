package ru.otus.sokolovsky.hw16.ms.message;

import java.util.Map;

public interface Message {
    String getDestination();

    Map<String, String> getHeaders();

    byte[] getBody();
}
