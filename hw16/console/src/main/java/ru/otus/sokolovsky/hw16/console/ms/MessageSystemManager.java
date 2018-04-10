package ru.otus.sokolovsky.hw16.console.ms;

import ru.otus.sokolovsky.hw16.integration.message.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageSystemManager implements Runnable {

    private final String host;
    private final int port;
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);

    public MessageSystemManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void createChannel(String name) {
        ParametrizedMessage message = new ParametrizedMessageImpl("service", "create-new-named-channel", MessageTypes.COMMAND_MESSAGE);
        message.setParameter("channel", name);
        queue.add(message);
    }

    public void run() {
        try (Socket socket = new Socket(host, port)) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Message message = queue.take(); // Blocks
                    OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                    PrintWriter sender = new PrintWriter(writer);
                    String json = MessageTransformer.toJson((ParametrizedMessage) message);
                    sender.println(json);
                    sender.println();
                    sender.flush();
                    System.out.println("sent: " + json);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
