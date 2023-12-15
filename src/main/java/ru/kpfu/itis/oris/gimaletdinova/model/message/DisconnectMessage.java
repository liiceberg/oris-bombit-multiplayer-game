package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DisconnectMessage extends Message {
    private int position;

    public DisconnectMessage(int position) {
        super(MessageType.DISCONNECT);
        this.position = position;
        setContent();
    }

    public DisconnectMessage(byte[] content) {
        super(MessageType.DISCONNECT, Arrays.copyOfRange(content, 1, content.length));
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
