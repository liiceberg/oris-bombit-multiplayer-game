package ru.kpfu.itis.oris.gimaletdinova.model;

public enum Mode {
    FIRST((byte) 1), SECOND((byte) 2), THIRD((byte) 3);
    private final byte value;
    Mode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }
    public static Mode getMode(byte b) {
        switch (b) {
            case 1 -> {
                return FIRST;
            }
            case 2 -> {
                return SECOND;
            }
            case 3 -> {
                return THIRD;
            }
        }
        return null;
    }

}
