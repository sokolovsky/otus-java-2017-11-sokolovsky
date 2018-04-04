package ru.otus.sokolovsky.hw16.ms.controller;

import java.net.Socket;

public class ExchangeListener extends AbstractListener {

    public ExchangeListener(int port) {
        super(port);
    }

    @Override
    protected void handleConnection(Socket socket) {
        System.out.println("I am listening to the socket " + socket);
    }
}
