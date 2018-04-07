package ru.otus.sokolovsky.hw16.ms.message;

import java.util.Map;

public interface Message {
    Map<String, String> getHeaders();

    byte[] getBody();
}
