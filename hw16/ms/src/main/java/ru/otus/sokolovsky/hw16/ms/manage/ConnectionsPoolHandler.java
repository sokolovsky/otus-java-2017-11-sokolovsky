package ru.otus.sokolovsky.hw16.ms.manage;

import ru.otus.sokolovsky.hw16.ms.channel.Channel;
import ru.otus.sokolovsky.hw16.ms.channel.PointToPointChannel;
import ru.otus.sokolovsky.hw16.ms.client.Connector;
import ru.otus.sokolovsky.hw16.ms.server.ConnectionHandler;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionsPoolHandler implements ConnectionHandler {

    private final ExecutorService threadPool;
    private SystemManager msManager;

    private static int connectionsIndex = 0;

    private Map<String, PointToPointChannel> connectionChannels = new HashMap<>();
    private Map<String, Connector> connectors = new HashMap<>();

    public ConnectionsPoolHandler(SystemManager systemManager) {
        msManager = systemManager;
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void handle(Socket socket) {
        System.out.println("MS got socket with port:" + socket.getPort());
        PointToPointChannel channel = msManager.createPointToPointChannel(getNewChannelName());
        connectionChannels.put(channel.getName(), channel);
        Connector connector = new Connector(socket, channel, msManager::routeMessage);
        connectors.put(channel.getName(), connector);
        threadPool.submit(connector);
    }

    public Channel getChannel(String name) {
        return connectionChannels.get(name);
    }

    private String getNewChannelName() {
        return String.format("Channel-%d", connectionsIndex++);
    }

    @Override
    public void close() {
        connectors.forEach((n, connector) -> {
            try {
                connector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
