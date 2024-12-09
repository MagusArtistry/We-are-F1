package com.example.wearef1;

public class Post {
    private String id;
    private String author;
    private String content;
    private long timestamp;

    // Empty constructor for Firebase
    public Post() {}

    public Post(String id, String author, String content, long timestamp) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
