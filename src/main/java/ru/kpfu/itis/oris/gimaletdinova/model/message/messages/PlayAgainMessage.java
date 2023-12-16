package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

public class PlayAgainMessage extends Message {
    public PlayAgainMessage() {
        super(MessageType.PLAY_AGAIN);
        setContent();
    }

    @Override
    protected void getContent() {}

    @Override
    protected void setContent() {
        content = new byte[0];
    }
}
