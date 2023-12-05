package ru.kpfu.itis.oris.gimaletdinova.util;

import ru.kpfu.itis.oris.gimaletdinova.model.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.MessageType;
import ru.kpfu.itis.oris.gimaletdinova.model.User;

import java.io.*;
import java.nio.ByteBuffer;

public class MessageConverter {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Message m = new Message(MessageType.CONNECT, new User("lic"), convert(4));
        byte[] b = convertToBytes(m);
        System.out.println(convertFromBytes(b));
    }
    public static byte[] convertToBytes(Message m) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(m);
            out.flush();
            return bos.toByteArray();
        }
    }

    public static Message convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return (Message) in.readObject();
        }
    }

    public static byte[] convert(int i) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        return b.array();
    }

    public static int wrap(byte[] b) {
        return ByteBuffer.wrap(b).getInt();
    }
}
