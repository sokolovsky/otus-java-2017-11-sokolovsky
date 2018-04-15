package ru.otus.sokolovsky.hw16.web.chat;

public class ChatMessage {
    private String author;
    private String time;
    private String text;

    public ChatMessage(String author, String time, String text) {
        this.author = author;
        this.time = time;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }
}
