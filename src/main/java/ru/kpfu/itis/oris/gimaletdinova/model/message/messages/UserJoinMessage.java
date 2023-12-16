package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class UserJoinMessage extends Message {
    private String username;

    public UserJoinMessage(String username) {
        super(MessageType.JOIN_USER);
        this.username = username;
        setContent();
    }

    public UserJoinMessage(byte[] content) {
        super(MessageType.JOIN_USER, Arrays.copyOfRange(content, 1, content.length));
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        StringBuilder builder = new StringBuilder();

        char c;
        while (buffer.hasRemaining() && (c = buffer.getChar()) != Character.MIN_VALUE) {
            builder.append(c);
        }

        username = builder.toString();
    }

    @Override
    protected void setContent() {
        ByteBuffer buffer = ByteBuffer.allocate(username.length() * 2);
        for (char c: username.toCharArray()) {
            buffer.putChar(c);
        }
        content = buffer.array();
    }

    public String getUsername() {
        return username;
    }

}
