package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConnectResponseMessage extends Message {
    private int position;
    public ConnectResponseMessage(Map<String, Object> content) {
        super(MessageType.CONNECT, content);
    }

    public ConnectResponseMessage(byte[] content) {
        super(MessageType.CONNECT, Arrays.copyOfRange(content, 1, content.length));
        Map<String, Object> map = getContent();
        position = (Integer) map.get("position");
    }

    @Override
    public Map<String, Object> getContent() {
        Map<String, Object> map = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(content);
        map.put("position", buffer.getInt());
        return map;
    }

    @Override
    public void setContent(Map<String, Object> map) {
        position = (Integer) map.get("position");
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(position);
        content = buffer.array();
    }

    public int getPosition() {
        return position;
    }
}