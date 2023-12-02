package ru.kpfu.itis.oris.gimaletdinova.util;

import ru.kpfu.itis.oris.gimaletdinova.model.Message;

import java.io.*;

public class MessageConverter {
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
}
