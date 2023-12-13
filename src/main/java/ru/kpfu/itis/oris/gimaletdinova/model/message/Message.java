package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Map;

public abstract class Message {
    private MessageType messageType;
    protected byte[] content;

    public Message(MessageType messageType, Map<String, Object> content) {
        this.messageType = messageType;
        setContent(content);
    }

    public Message(MessageType messageType, byte[] content) {
        this.messageType = messageType;
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    public byte[] getByteContent() {
        ByteBuffer buffer = ByteBuffer.allocate(content.length + 1);
        buffer.put(messageType.getValue());
        buffer.put(content);
        return buffer.array();
    }

    public abstract Map<String, Object> getContent();
    public abstract void setContent(Map<String, Object> map);

    @Override
    public String toString() {
        return messageType.name();
    }
}
