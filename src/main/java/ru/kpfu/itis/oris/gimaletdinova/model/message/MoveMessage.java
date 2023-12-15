package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class MoveMessage extends Message {
    private int code;
    private int playerPosition;

    public MoveMessage(int code, int playerPosition) {
        super(MessageType.MOVE);
        this.code = code;
        this.playerPosition = playerPosition;
        setContent();
    }

    public MoveMessage(byte[] content) {
        super(MessageType.MOVE, Arrays.copyOfRange(content, 1, content.length));
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        code = buffer.getInt();
        playerPosition = buffer.getInt();
    }

    @Override
    protected void setContent() {
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
