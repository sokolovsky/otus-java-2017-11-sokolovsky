package ru.otus.sokolovsky.hw16.ms.client;

import ru.otus.sokolovsky.hw16.integration.message.IllegalFormatException;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageTransformer;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;
import ru.otus.sokolovsky.hw16.ms.channel.PointToPointChannel;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Logger;

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
        ParametrizedMessage pMessage = (ParametrizedMessage) message;
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            writer.write(MessageTransformer.toJson(pMessage));
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
                ParametrizedMessage message;
                try {
                    message = MessageTransformer.fromJson(row);
                } catch (IllegalFormatException e) {
                    e.printStackTrace();
                    continue;
                }
                message.setSource(channel.getName());
                messageReceiver.accept(message);
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage() + ", Channel: " + channel.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
