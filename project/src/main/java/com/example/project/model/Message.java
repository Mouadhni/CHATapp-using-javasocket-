package com.example.project.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String messageContent;
    private Timestamp timestamp;

    public Message(int messageId, int senderId, int receiverId, String messageContent, Timestamp timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }
    public Message(String messageContent) {

        this.messageContent = messageContent;
        this.timestamp =Timestamp.valueOf(LocalDateTime.now()); ;
    }

    // Getters and Setters for Message attributes
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
