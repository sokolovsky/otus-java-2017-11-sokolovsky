package ru.otus.sokolovsky.hw16.ms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerListener extends AbstractListener {
    private Socket socket;

    public ControllerListener(int port) {
        super(port);
    }

    @Override
    protected void handleConnection(Socket socket) {
        this.socket = socket;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::receive);
    }

    private void receive() {
        try {
            InputStream inputStream = this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
