package ru.otus.sokolovsky.hw16.ms.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ServerListenerImpl implements ServerListener {

    private ServerSocket socket;
    private ExecutorService threadExecutor;
    private List<ConnectionHandler> handlers = new ArrayList<>();

    private static Logger logger = Logger.getLogger(ServerListenerImpl.class.getName());

    /**
     *
     * @throws IOException Occurs due to network connections
     * @param port
     */
    public void startListening(int port) {
        validPort(port);
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadExecutor = Executors.newSingleThreadExecutor();
        threadExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket connection = socket.accept();
                    handlers.forEach(handler -> handler.handle(connection));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        logger.info("Start listening on port: " + port);
    }

    private void validPort(int port) {
        if (port < 0 || port > (1 << 16)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void addConnectionHandler(ConnectionHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void setConnectionHandlers(List<ConnectionHandler> handlers) {

    }

    public void dispose() throws IOException {
        if (socket == null) {
            return;
        }
        socket.close();
        threadExecutor.shutdown();
    }
}
