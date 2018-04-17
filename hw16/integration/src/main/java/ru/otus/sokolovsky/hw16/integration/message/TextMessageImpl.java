package ru.otus.sokolovsky.hw16.integration.message;

public class TextMessageImpl extends AbstractMessage implements TextMessage {
    private String body;

    public TextMessageImpl(String destination, String name, MessageType type) {
        super(destination, name, type);
    }

    @Override
    public byte[] getBody() {
        return new byte[0];
    }

    @Override
    public String getBodyAsString() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }
}
