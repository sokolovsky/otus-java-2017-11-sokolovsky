package ru.otus.sokolovsky.hw16.web.chat;

public class ChatServiceImpl implements ChatService {

    private ChatServer chatServer;

    public ChatServiceImpl(ChatServer chatServer) {
        this.chatServer = chatServer;
        chatServer.registerMessageHandler(this::handleRequest);
        chatServer.registerConnectionHandler(this::handleNewConnection);
    }

    private void handleNewConnection(String login) {
        System.out.println("To handle connection with: " + login);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(String json) {
        System.out.println("To handle request: " + json);
    }

    private String messageToJson(String message) {
//        Gson gson = new GsonBuilder().create();
//        Map<String, String> map = new HashMap<String, String>() {{
//            put("login", message.getAuthor());
//            put("time", message.getTime().toString());
//            put("message", message.getText());
//        }};
//        // Gson has one bug with serialize of maps
//        return gson.toJson(new HashMap<>(map));
        return "";
    }

    public void pushMessage(String message) {
        chatServer.sendAll(messageToJson(message));
    }

    @Override
    public void pushMessage(String message, String destination) {
        chatServer.send(messageToJson(message), destination);
    }
}
