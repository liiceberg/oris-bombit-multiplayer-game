package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class GameStartMessage extends Message {
    private int[] characters;

    public GameStartMessage(int[] characters) {
        super(MessageType.START_GAME);
        this.characters = characters;
        setContent();
    }

    public GameStartMessage(byte[] content) {
        super(MessageType.START_GAME, Arrays.copyOfRange(content, 1, content.length));
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        ArrayList<Integer> characters = new ArrayList<>();
        int i;
        while (buffer.hasRemaining() && (i = buffer.getInt()) != Integer.MIN_VALUE) {
            characters.add(i);
        }
        this.characters = new int[characters.size()];
        for (i = 0; i < characters.size(); i++) {
            this.characters[i] = characters.get(i);
        }
    }

    @Override
    protected void setContent() {
        ByteBuffer buffer = ByteBuffer.allocate(characters.length * 4 + 4);
        for (int c: characters) {
            buffer.putInt(c);
        }
        buffer.putInt(Integer.MIN_VALUE);
        content = buffer.array();
    }

    public int[] getCharacters() {
        return characters;
    }

}
