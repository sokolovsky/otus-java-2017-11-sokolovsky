package ru.otus.sokolovsky.hw16.web;

import java.io.IOException;
import java.net.ServerSocket;

public class FreePortFinder {

    private int port;

    public FreePortFinder(int initialPort) {
        for (int i = initialPort; i < 60_000; i++) {
            try {
                new ServerSocket(i);
                initialPort = i;
                break;
            } catch (IOException e) {
                // Use exception only for except situations
            }
        }
        this.port = initialPort;
    }

    public int getPort() {
        return port;
    }
}
