package ru.otus.sokolovsky.hw16.integration.message;

import java.util.Map;

public interface ParametrizedMessage extends Message {
    Map<String, String> getParameters();

    void setParameter(String name, String value);
}
