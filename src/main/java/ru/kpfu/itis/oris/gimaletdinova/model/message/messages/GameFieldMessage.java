package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.Block;
import ru.kpfu.itis.oris.gimaletdinova.model.Mode;
import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static ru.kpfu.itis.oris.gimaletdinova.util.GameFieldRepository.*;

public class GameFieldMessage extends Message {
    private Mode fieldMode;
    private Mode obstaclesMode;
    private Block[][] obstacles;
    public GameFieldMessage(Block[][] obstacles, Mode fieldMode, Mode obstaclesMode) {
        super(MessageType.GAME_FIELD);
        this.fieldMode = fieldMode;
        this.obstaclesMode = obstaclesMode;
        this.obstacles = obstacles;
        setContent();
    }

    public GameFieldMessage(byte[] content) {
        super(MessageType.GAME_FIELD, Arrays.copyOfRange(content, 1, content.length));
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        fieldMode = Mode.getMode(buffer.get());
        obstaclesMode = Mode.getMode(buffer.get());
        obstacles = new Block[HEIGHT][WIDTH];
        byte i;
        while (buffer.hasRemaining() && (i = buffer.get()) != 0) {
            byte j = buffer.get();
            obstacles[i][j] = Block.OBSTACLE;
        }
    }

    @Override
    protected void setContent() {
        ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + OBSTACLES_COUNT * 2);
        buffer.put(fieldMode.getValue());
        buffer.put(obstaclesMode.getValue());
        int c = 0;
        for (byte i = 0; i < obstacles.length; i++) {
            for (byte j = 0; j < obstacles[0].length; j++) {
                if (obstacles[i][j] == Block.OBSTACLE) {
                    c++;
                    buffer.put(i);
                    buffer.put(j);
                }
            }
        }
        content = buffer.array();
    }

    public Mode getFieldMode() {
        return fieldMode;
    }

    public Mode getObstaclesMode() {
        return obstaclesMode;
    }

    public Block[][] getObstacles() {
        return obstacles;
    }
}
