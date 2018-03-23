package ru.otus.sokolovsky.hw15.chat;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketChatServerImpl extends org.java_websocket.server.WebSocketServer implements ChatServer {
    private static Pattern channelTestPattern = Pattern.compile("^/chat-");

    private List<Consumer<String>> messageHandlers = new ArrayList<>();

    public WebSocketChatServerImpl(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Connect: " + clientHandshake.getResourceDescriptor());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("WebSocket server is closed");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("Message received: " + s);
        String resource = webSocket.getResourceDescriptor();
        Optional<String> loginOptional = parseLogin(resource);
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
    public void sendAll(String message) {
        this.broadcast(message);
    }

    private Optional<String> parseLogin(String str) {
        Matcher matcher = channelTestPattern.matcher(str);
        if (!matcher.find()) {
            return Optional.empty();
        }
        return Optional.of(matcher.replaceAll(""));
    }
}
