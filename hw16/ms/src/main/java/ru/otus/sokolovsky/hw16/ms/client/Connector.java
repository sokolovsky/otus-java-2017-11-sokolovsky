package ru.otus.sokolovsky.hw16.ms.client;

import ru.otus.sokolovsky.hw16.integration.message.ListParametrizedMessage;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageTransformer;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;
import ru.otus.sokolovsky.hw16.ms.channel.PointToPointChannel;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Used in another thread
 */
public class Connector implements Runnable, AutoCloseable{
    private Socket socket;
    private PointToPointChannel channel;
    private Consumer<Message> messageReceiver;

    public Connector(Socket socket, PointToPointChannel privateChannel, Consumer<Message> messageReceiver) {
        this.socket = socket;
        channel = privateChannel;
        this.messageReceiver = messageReceiver;
        channel.registerHandler(this::sendMessage);
    }

    private void sendMessage(Message message) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            if (message instanceof ParametrizedMessage) {
                writer.write(MessageTransformer.toJson((ParametrizedMessage) message));
            }
            if (message instanceof ListParametrizedMessage) {
                writer.write(MessageTransformer.toJson((ListParametrizedMessage) message));
            }
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        System.out.println(channel.getName() + ": " + message);
    }

    @Override
    public void run() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream())) {
            log("Start listening stream, thread - " + Thread.currentThread().toString());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (socket.isConnected()) {
                String row = bufferedReader.readLine(); // Blocks
                log("Catch message: "+ row);
                if (row.trim().length() == 0) {
                    continue;
                }
                Message message;
                message = MessageTransformer.fromJson(row);
                message.setSource(channel.getName());
                messageReceiver.accept(message);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage() + ", Channel: " + channel.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
