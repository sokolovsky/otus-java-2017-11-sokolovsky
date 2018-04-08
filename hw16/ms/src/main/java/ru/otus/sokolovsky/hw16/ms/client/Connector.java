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
    private static Logger logger = Logger.getLogger(Connector.class.getName());

    public Connector(Socket socket, PointToPointChannel privateChannel, Consumer<Message> messageReceiver) {
        this.socket = socket;
        channel = privateChannel;
        this.messageReceiver = messageReceiver;
        channel.registerHandler(this::sendMessage);
    }

    private void sendMessage(Message message) {
        ParametrizedMessage pMessage = (ParametrizedMessage) message;
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream())) {
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            writer.write(MessageTransformer.toJson(pMessage));
            writer.newLine();
            socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream())) {
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String row = bufferedReader.readLine();
                logger.info("Read message: " + row);
                ParametrizedMessage message = MessageTransformer.fromJson(row);
                message.setSource(channel.getName());
                messageReceiver.accept(message);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (IllegalFormatException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
