package ru.otus.sokolovsky.hw16.ms.manage;

import ru.otus.sokolovsky.hw16.ms.server.ConnectionHandler;

import java.net.Socket;

public class ConnectionsPoolHandler implements ConnectionHandler {
    @Override
    public void handle(Socket socket) {

    }

    @Override
    public void close() {
        // need to close socket connection
    }
}
