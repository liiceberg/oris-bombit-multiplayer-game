package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DisconnectMessage extends Message {
    private int position;

    public DisconnectMessage(Map<String, Object> content) {
        super(MessageType.DISCONNECT, content);
    }
    public DisconnectMessage(byte[] content) {
        super(MessageType.DISCONNECT, Arrays.copyOfRange(content, 1, content.length));
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

    public int getPlayerPosition() {
        return position;
    }
}
