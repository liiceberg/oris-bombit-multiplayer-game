package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

import java.nio.ByteBuffer;
import java.util.Arrays;
public class LoseMessage extends Message {
    private int position;

    public LoseMessage(int position) {
        super(MessageType.LOSE);
        this.position = position;
        setContent();
    }

    public LoseMessage(byte[] content) {
        super(MessageType.LOSE, Arrays.copyOfRange(content, 1, content.length));
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        position = buffer.getInt();
    }

    @Override
    protected void setContent() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(position);
        content = buffer.array();
    }

    public int getPlayerPosition() {
        return position;
    }
}
