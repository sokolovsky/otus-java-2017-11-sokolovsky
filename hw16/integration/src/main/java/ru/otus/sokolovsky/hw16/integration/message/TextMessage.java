package ru.otus.sokolovsky.hw16.integration.message;

public interface TextMessage extends Message {

    void setBody(String body);

    String getBodyAsString();
}
