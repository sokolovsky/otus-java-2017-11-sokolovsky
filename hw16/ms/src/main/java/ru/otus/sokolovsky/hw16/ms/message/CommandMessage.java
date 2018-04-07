package ru.otus.sokolovsky.hw16.ms.message;

import java.util.Map;

public interface CommandMessage extends Message {
    String getCommand();

    Map<String, String> getArgs();

    Message createAnswer();
}
