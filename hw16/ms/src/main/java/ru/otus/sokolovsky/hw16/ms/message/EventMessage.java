package ru.otus.sokolovsky.hw16.ms.message;

import java.util.Map;

public interface EventMessage extends Message {
    String name();

    Map<String, String> getParams();
}
