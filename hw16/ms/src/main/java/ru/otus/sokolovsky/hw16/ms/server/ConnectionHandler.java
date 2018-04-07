package ru.otus.sokolovsky.hw16.ms.server;

import java.net.Socket;

public interface ConnectionHandler extends AutoCloseable {
    void handle(Socket socket);

    void close();
}
