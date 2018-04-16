package ru.otus.sokolovsky.hw16.web.chat;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import ru.otus.sokolovsky.hw16.integration.client.Connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketChatServerImpl extends org.java_websocket.server.WebSocketServer implements ChatServer {

    private static Pattern channelTestPattern = Pattern.compile("^/chat-");

    private List<Consumer<String>> messageHandlers = new ArrayList<>();
    private List<Consumer<String>> connectHandlers = new ArrayList<>();
    private Map<String, WebSocket> activeSockets = new HashMap<>();

    public WebSocketChatServerImpl(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Connect: " + clientHandshake.getResourceDescriptor());

        Optional<String> loginOptional = parseLogin(webSocket);
        if (!loginOptional.isPresent()) {
            return;
        }
        activeSockets.put(loginOptional.get(), webSocket);
        connectHandlers.forEach(h -> h.accept(loginOptional.get()));
        System.out.println("Connection handlers  send to accept.");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("WebSocket server is closed");
        Optional<String> loginOptional = parseLogin(webSocket);
        if (!loginOptional.isPresent()) {
            return;
        }

        activeSockets.remove(loginOptional.get());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("Message received: " + s);
        Optional<String> loginOptional = parseLogin(webSocket);
        if (!loginOptional.isPresent()) {
            return;
        }

        String json = injectLoginToJsonMessage(loginOptional.get(), s);
        messageHandlers.forEach(h -> h.accept(json));
        System.out.println("Message send to accept.");
    }

    private String injectLoginToJsonMessage(String login, String json) {
        return json.replaceAll("^\\{", "{ \"login\":\"" + login + "\", ");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("Error was accused: " + e);
    }

    @Override
    public void onStart() {
        System.out.printf("\n\nWebSocket server is started. Port: %d\n\n", getPort());
    }

    @Override
    public void registerMessageHandler(Consumer<String> handler) {
        messageHandlers.add(handler);
    }

    @Override
    public void registerConnectionHandler(Consumer<String> handler) {
        connectHandlers.add(handler);
    }

    @Override
    public void sendAll(String message) {
        this.broadcast(message);
    }

    @Override
    public void send(String message, String destination) {
        System.out.printf("send %s to %s\n", message, destination);
        WebSocket webSocket = activeSockets.get(destination);
        if (webSocket == null) {
            System.out.println("But socket is null");
            return;
        }
        webSocket.send(message);
    }

    @Override
    public void listen() {
        System.out.println("Start listening port " + getPort());
        this.start();
    }

    @Override
    public void close() {
        try {
            this.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Optional<String> parseLogin(String str) {
        Matcher matcher = channelTestPattern.matcher(str);
        if (!matcher.find()) {
            return Optional.empty();
        }
        return Optional.of(matcher.replaceAll(""));
    }

    private Optional<String> parseLogin(WebSocket webSocket) {
        String resource = webSocket.getResourceDescriptor();
        return parseLogin(resource);
    }
}
