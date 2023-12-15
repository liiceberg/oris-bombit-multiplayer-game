package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class AddBombMessage extends Message {
    private int x;
    private int y;
    private int playerPosition;

    public AddBombMessage(int x, int y, int playerPosition) {
        super(MessageType.ADD_BOMB);
        this.x = x;
        this.y = y;
        this.playerPosition = playerPosition;
        setContent();
    }

    public AddBombMessage(byte[] content) {
        super(MessageType.ADD_BOMB, Arrays.copyOfRange(content, 1, content.length));
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        x = buffer.getInt();
        y = buffer.getInt();
        playerPosition = buffer.getInt();
    }

    @Override
    protected void setContent() {
        ByteBuffer buffer = ByteBuffer.allocate(4 * 3);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.putInt(playerPosition);
        content = buffer.array();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }
}
