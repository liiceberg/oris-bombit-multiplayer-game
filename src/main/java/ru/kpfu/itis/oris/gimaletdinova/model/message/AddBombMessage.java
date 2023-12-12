package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddBombMessage extends Message {
    private int x;
    private int y;
    private int playerPosition;
    public AddBombMessage(Map<String, Object> content) {
        super(MessageType.ADD_BOMB, content);
    }

    public AddBombMessage(byte[] content) {
        super(MessageType.ADD_BOMB, Arrays.copyOfRange(content, 1, content.length));
        Map<String, Object> map = getContent();
        x = (Integer) map.get("x");
        y = (Integer) map.get("y");
        playerPosition = (Integer) map.get("position");
    }

    @Override
    public Map<String, Object> getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        Map<String, Object> map = new HashMap<>();
        map.put("x", buffer.getInt());
        map.put("y", buffer.getInt());
        map.put("position", buffer.getInt());
        return map;
    }

    @Override
    public void setContent(Map<String, Object> map) {
        x = (Integer) map.get("x");
        y = (Integer) map.get("y");
        playerPosition = (Integer) map.get("position");
        ByteBuffer buffer = ByteBuffer.allocate(4 * 3);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.putInt(playerPosition);
        content = buffer.array();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }
}
