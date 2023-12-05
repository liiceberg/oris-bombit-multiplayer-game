package ru.kpfu.itis.oris.gimaletdinova.client;

import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.model.Message;
import ru.kpfu.itis.oris.gimaletdinova.util.MessageConverter;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.*;
import java.net.*;

import static ru.kpfu.itis.oris.gimaletdinova.server.GameServer.BUFFER_LENGTH;

public class ClientPlayer implements Closeable, Runnable {
    private final GameApplication application;
    private final DatagramSocket socket;
    private final byte[] buffer = new byte[BUFFER_LENGTH];
    private final InetAddress address;
    private final int port;

    public ClientPlayer(GameApplication application, String room) {
        this.application = application;
        port = RoomRepository.getPort(room);
        try {
            socket = new DatagramSocket();
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                byte[] data = packet.getData();
                Message message = MessageConverter.convertFromBytes(data);
                switch (message.getMessageType()) {
                    case CONNECT -> {

                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Message send(Message message) {
        try {
            byte[] data = MessageConverter.convertToBytes(message);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            return MessageConverter.convertFromBytes(packet.getData());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        socket.close();
    }

}
