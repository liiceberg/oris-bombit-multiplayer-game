package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ConnectResponseMessage extends Message {
    private int position;

    public ConnectResponseMessage(int position) {
        super(MessageType.CONNECT);
        this.position = position;
        setContent();
    }
    public ConnectResponseMessage(byte[] content) {
        super(MessageType.CONNECT, Arrays.copyOfRange(content, 1, content.length));
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

    public int getPosition() {
        return position;
    }
}
