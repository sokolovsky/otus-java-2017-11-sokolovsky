package ru.otus.sokolovsky.hw16.ms.message;

public interface RequestReplyMessage {
    String getFrom();

    Message createAnswer();
}
