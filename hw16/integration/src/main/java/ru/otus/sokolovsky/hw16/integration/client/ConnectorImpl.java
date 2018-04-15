package ru.otus.sokolovsky.hw16.integration.client;

import ru.otus.sokolovsky.hw16.integration.message.IllegalFormatException;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageTransformer;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ConnectorImpl implements Connector {
    private int port;
    private Socket socket;
    private List<Consumer<Message>> handlers = new LinkedList<>();
    private BlockingQueue<Message> dispatchQueue = new ArrayBlockingQueue<>(10);

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void connect() {
        try  {
            socket = new Socket("localhost", port);
            ExecutorService threadPool = Executors.newCachedThreadPool();
            threadPool.submit(new TackingWorker(socket));
            threadPool.submit(new SenderWorker(socket));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void setHandlers(List<Consumer<Message>> handlers) {
        this.handlers = handlers;
    }

    private void arise(Message message) {
        handlers.forEach(c -> c.accept(message));
    }

    @Override
    public void sendMessage(Message message) {
        dispatchQueue.add(message);
    }

    @Override
    public Message sendMessageAndWaitResponse(Message message, int waitingInSec) throws InterruptedException {
        String code = "" + System.identityHashCode(message);
        message.setHeader("i-hash-code", code);
        sendMessage(message);
        BlockingQueue<Message> block = new LinkedBlockingQueue<>();
        Consumer<Message> handler = (Message responseMessage) -> {
            if (responseMessage.getHeaders().get("i-hash-code").equals(code)) {
                block.add(responseMessage);
            }
        };
        addMessageHandler(handler);
        Message response = block.poll(waitingInSec, TimeUnit.SECONDS);
        handlers.remove(handler);
        return response;
    }

    @Override
    public void addMessageHandler(Consumer<Message> handler) {
        handlers.add(handler);
    }

    public void setDispatchQueue(BlockingQueue<Message> dispatchQueue) {
        this.dispatchQueue = dispatchQueue;
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SenderWorker implements Runnable {

        private Socket socket;

        SenderWorker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while (socket.isConnected()) {
                    try {
                        Message message = dispatchQueue.take(); // Blocks
                        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                        PrintWriter sender = new PrintWriter(writer);
                        String json = MessageTransformer.toJson((ParametrizedMessage) message);
                        sender.println(json);
                        sender.println();
                        sender.flush();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private class TackingWorker implements Runnable {

        private Socket socket;

        TackingWorker(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (socket.isConnected()) {
                    String row = bufferedReader.readLine(); // Blocks
                    if (row.trim().length() == 0) {
                        continue;
                    }
                    Message message;
                    try {
                        message = MessageTransformer.fromJson(row);
                        arise(message);
                    } catch (IllegalFormatException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
