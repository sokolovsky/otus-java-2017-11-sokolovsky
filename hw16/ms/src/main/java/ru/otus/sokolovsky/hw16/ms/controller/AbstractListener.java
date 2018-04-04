package ru.otus.sokolovsky.hw16.ms.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public abstract class AbstractListener {

    private int port;
    private ServerSocket socket;
    private ExecutorService threadExecutor;

    private static Logger logger = Logger.getLogger("Main listener");

    public AbstractListener(int port) {
        if (port < 0 || port > (1 << 16)) {
            throw new IllegalArgumentException();
        }
        this.port = port;
    }

    /**
     *
     * @throws IOException Occurs due to network connections
     */
    public void start() throws IOException {
        this.socket = new ServerSocket(this.port);
        threadExecutor = Executors.newSingleThreadExecutor();
        threadExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket connection = socket.accept();
                    handleConnection(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        logger.info("Start listening on port: " + port);
    }

    public void dispose() throws IOException {
        if (socket == null) {
            return;
        }
        socket.close();
        threadExecutor.shutdown();
    }

    protected abstract void handleConnection(Socket socket);
}
