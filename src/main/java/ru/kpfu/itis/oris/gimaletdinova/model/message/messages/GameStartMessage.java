package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class GameStartMessage extends Message {
    private String[] users;
    private int[] characters;

    public GameStartMessage(String[] users, int[] characters) {
        super(MessageType.START_GAME);
        this.users = users;
        this.characters = characters;
        setContent();
    }

    public GameStartMessage(byte[] content) {
        super(MessageType.START_GAME, Arrays.copyOfRange(content, 1, content.length));
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        ArrayList<String> users = new ArrayList<>();
        ArrayList<Integer> characters = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        char c;
        while ((c = buffer.getChar()) != Character.MIN_VALUE) {
            while (c != Character.MIN_VALUE) {
                builder.append(c);
                c = buffer.getChar();
            }
            users.add(builder.toString());
            builder = new StringBuilder();
        }
        this.users = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            this.users[i] = users.get(i);
        }

        int i;
        while ((i = buffer.getInt()) != 0 && buffer.hasRemaining()) {
            characters.add(i);
        }

        this.characters = new int[characters.size()];
        for (i = 0; i < characters.size(); i++) {
            this.characters[i] = characters.get(i);
        }
    }

    @Override
    protected void setContent() {
        int capacity = 0;
        for (String u: users) {
            capacity += u.length() + 1;
        }
        ByteBuffer buffer = ByteBuffer.allocate(capacity * 2 + characters.length * 4 + 2);
        for (String u: users) {
            for (char c: u.toCharArray()) {
                buffer.putChar(c);
            }
            buffer.putChar(Character.MIN_VALUE);
        }
        buffer.putChar(Character.MIN_VALUE);
        for (int c: characters) {
            buffer.putInt(c);
        }
        content = buffer.array();
//        max 146
    }

    public String[] getUsers() {
        return users;
    }

    public int[] getCharacters() {
        return characters;
    }

}
