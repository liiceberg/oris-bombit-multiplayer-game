package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MoveMessage extends Message {
    private int code;
    private int playerPosition;
    public MoveMessage(Map<String, Object> content) {
        super(MessageType.MOVE, content);
    }

    public MoveMessage(byte[] content) {
        super(MessageType.MOVE, Arrays.copyOfRange(content, 1, content.length));
        Map<String, Object> map = getContent();
        code = (Integer) map.get("code");
        playerPosition = (Integer) map.get("position");
    }

    @Override
    public Map<String, Object> getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        Map<String, Object> map = new HashMap<>();
        map.put("code", buffer.getInt());
        map.put("position", buffer.getInt());
        return map;
    }

    @Override
    public void setContent(Map<String, Object> map) {
        code = (Integer) map.get("code");
        playerPosition = (Integer) map.get("position");
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(code);
        buffer.putInt(playerPosition);
        content = buffer.array();
    }

    public Integer getCode() {
        return code;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }
}
