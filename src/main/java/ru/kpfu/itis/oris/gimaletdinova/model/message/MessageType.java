package ru.kpfu.itis.oris.gimaletdinova.model.message;

public enum MessageType {
    CONNECT((byte) 0),
    START_GAME ((byte) 1),
    ADD_BOMB((byte) 2),
    MOVE((byte) 3),
    LOSE((byte) 4),
    DISCONNECT((byte) 5),
    JOIN_USER((byte) 6);
    private final byte value;
    MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }
}
