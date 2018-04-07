package ru.otus.sokolovsky.hw16.ms.server;

import java.util.List;

public interface ServerListener {
    void startListening(int port);

    void addConnectionHandler(ConnectionHandler handler);

    void setConnectionHandlers(List<ConnectionHandler> handlers);
}
