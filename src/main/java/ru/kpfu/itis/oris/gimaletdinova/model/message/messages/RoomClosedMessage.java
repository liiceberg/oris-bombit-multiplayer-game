package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

public class RoomClosedMessage extends Message {
    public RoomClosedMessage() {
        super(MessageType.CLOSE_ROOM);
        setContent();
    }

    @Override
    protected void getContent() {}

    @Override
    protected void setContent() {
        content = new byte[0];
    }
}
