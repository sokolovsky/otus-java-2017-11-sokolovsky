package ru.otus.sokolovsky.hw16.ms.manage;

import ru.otus.sokolovsky.hw16.ms.channel.ChannelFactory;
import ru.otus.sokolovsky.hw16.ms.server.ConnectionHandler;

import java.net.Socket;

public class ConnectionsPoolHandler implements ConnectionHandler {

    private ChannelFactory channelFactory;

    public ConnectionsPoolHandler(ChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    @Override
    public void handle(Socket socket) {
        // use private channel
    }

    @Override
    public void close() {
        // need to close socket connection
    }
}
