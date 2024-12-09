package com.example.wearef1;

public class Comment {
    private String author;
    private String content;
    private long timestamp;

    // Empty constructor for Firebase
    public Comment() {}

    public Comment(String author, String content, long timestamp) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
