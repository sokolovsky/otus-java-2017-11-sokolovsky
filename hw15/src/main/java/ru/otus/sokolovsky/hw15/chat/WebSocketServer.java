package ru.otus.sokolovsky.hw15.chat;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {
    private static Pattern channelTestPattern = Pattern.compile("^/chat-");

    public WebSocketServer(int port) {
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
        Matcher matcher = channelTestPattern.matcher(resource);
        if (!matcher.find()) {
            return;
        }
        String login = matcher.replaceFirst("");
        System.out.printf("\nПостановка в очередь сообщения от %s c текстом: %s\n", login, s);

        Map<String, Object> mParcel = Parcel.createMap(s);
        // получить json
        // отправить сервису бд через систему сообщений в отдельном потоке
        // для тестирования интерфейса оповещения тут не должно быть
        this.broadcast(
                "{\"sender\": \""+login+"\", \"message\": \"" + mParcel.get("message") + "\", \"time\": \"10: 21\"}"
        );
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("Error was accused: " + e);
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server is started");
    }
}
