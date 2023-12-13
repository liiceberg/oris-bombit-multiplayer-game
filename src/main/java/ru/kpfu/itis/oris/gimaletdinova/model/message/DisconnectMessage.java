package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.util.Map;

public class DisconnectMessage extends Message {

    public DisconnectMessage(Map<String, Object> content) {
        super(MessageType.DISCONNECT, content);
    }

    @Override
    public Map<String, Object> getContent() {
        return null;
    }

    @Override
    public void setContent(Map<String, Object> map) {
        content = new byte[0];
    }
}
