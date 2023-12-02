package ru.kpfu.itis.oris.gimaletdinova.client;

import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.model.Message;
import ru.kpfu.itis.oris.gimaletdinova.util.MessageConverter;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.*;
import java.net.*;

public class ClientPlayer implements Closeable {
    private final GameApplication application;
    private final DatagramSocket socket;
    private byte[] buffer;
    private final InetAddress address;
    private final int port;

    public ClientPlayer(GameApplication application, String room){
        this.application = application;
        port = RoomRepository.getPort(room);
        try {
            socket = new DatagramSocket();
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }

    }

    public Message send(Message message){
        try {
            buffer = MessageConverter.convertToBytes(message);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
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

    static class ClientThread implements Runnable {
        private final ClientPlayer clientPlayer;

        public ClientThread(ClientPlayer clientPlayer) {
            this.clientPlayer = clientPlayer;
        }

        @Override
        public void run() {
            while (true) {
//                listen messages??
            }
        }
    }
}
