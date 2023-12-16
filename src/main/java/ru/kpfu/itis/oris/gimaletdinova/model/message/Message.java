package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;

public abstract class Message {
    private final MessageType messageType;
    protected byte[] content;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public Message(MessageType messageType, byte[] content) {
        this.messageType = messageType;
        this.content = content;
        getContent();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public byte[] getByteContent() {
        ByteBuffer buffer = ByteBuffer.allocate(content.length + 1);
        buffer.put(messageType.getValue());
        buffer.put(content);
        return buffer.array();
    }

    protected abstract void getContent();
    protected abstract void setContent();

    @Override
    public String toString() {
        return messageType.name();
    }
}
