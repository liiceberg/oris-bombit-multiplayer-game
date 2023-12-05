package ru.kpfu.itis.oris.gimaletdinova.model;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private MessageType messageType;
    private byte[] content;
    private User user;

    public Message(MessageType messageType, User user) {
        this.messageType = messageType;
        this.user = user;
    }

    public Message(MessageType messageType, User user, byte[] content) {
        this.messageType = messageType;
        this.content = content;
        this.user = user;
    }



    public Message(byte[] content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", user=" + user +
                '}';
    }
}
