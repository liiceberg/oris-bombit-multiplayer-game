package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

public class FullRoomMessage extends Message {
    public FullRoomMessage() {
        super(MessageType.FULL_ROOM);
        setContent();
    }

    public FullRoomMessage(byte[] content) {
        super(MessageType.FULL_ROOM, content);
    }

    @Override
    protected void getContent() {
    }

    @Override
    protected void setContent() {
        content = new byte[0];
    }
}
