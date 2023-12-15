package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class UserJoinMessage extends Message {
    private String username;
    private int character;
    public UserJoinMessage(String username, int character) {
        super(MessageType.JOIN_USER);
        this.username = username;
        this.character = character;
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
        while ((c = buffer.getChar()) != Character.MIN_VALUE) {
            builder.append(c);
        }

        username = builder.toString();
        character = buffer.getInt();
    }

    @Override
    protected void setContent() {
        ByteBuffer buffer = ByteBuffer.allocate(username.length() * 2 + 2 + 4);
        for (char c: username.toCharArray()) {
            buffer.putChar(c);
        }
        buffer.putChar(Character.MIN_VALUE);
        buffer.putInt(character);
        content = buffer.array();
    }

    public String getUsername() {
        return username;
    }

    public int getCharacter() {
        return character;
    }
}
