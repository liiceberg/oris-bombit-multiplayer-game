package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameStartMessage extends Message {
    private String[] users;
    private int[] characters;

    public GameStartMessage(Map<String, Object> content) {
        super(MessageType.START_GAME, content);
    }

    public GameStartMessage(byte[] content) {
        super(MessageType.START_GAME, Arrays.copyOfRange(content, 1, content.length));
        Map<String, Object> map = getContent();
        Object[] o = (Object[]) map.get("users");
        users = new String[o.length];
        for (int i = 0; i < o.length; i++) {
            users[i] = (String) o[i];
        }
        o = (Object[]) map.get("characters");
        characters = new int[o.length];
        for (int i = 0; i < o.length; i++) {
            characters[i] = (int) o[i];
        }
    }

    @Override
    public Map<String, Object> getContent() {
        Map<String, Object> map = new HashMap<>();
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
        map.put("users", users.toArray());
        int i;
        while ((i = buffer.getInt()) != 0 && buffer.hasRemaining()) {
            characters.add(i);
        }
        map.put("characters", characters.toArray());
        return map;
    }

    @Override
    public void setContent(Map<String, Object> map) {
        users = (String[]) map.get("users");
        characters = (int[]) map.get("characters");
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
