package ru.kpfu.itis.oris.gimaletdinova.client;

import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.exceptions.RoomNotFoundException;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.*;
import java.net.*;

import static ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType.START_GAME;
import static ru.kpfu.itis.oris.gimaletdinova.server.GameServer.BUFFER_LENGTH;

public class ClientPlayer implements Closeable {
    private final GameApplication application;
    private final DatagramSocket socket;
    private final byte[] buffer = new byte[BUFFER_LENGTH];
    private final InetAddress address;
    private final Integer port;

    public ClientPlayer(GameApplication application, String room) throws RoomNotFoundException {
        this.application = application;
        port = RoomRepository.getPort(room);
        if (port == null) {
            throw new RoomNotFoundException();
        }
        address = RoomRepository.getAddress(room);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        new Thread(new ServerListener(this)).start();
    }



    public void send(Message message) {
        try {
            byte[] data = message.getByteContent();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseMessage(byte[] data) throws IOException, ClassNotFoundException {
        if (data[0] == START_GAME.getValue()) {
            System.out.println("start game message accepted");
            GameStartMessage message = new GameStartMessage(data);
            application.startGame(message.getUsers(), message.getCharacters());
        }
    }

    public int getPort() {
        return socket.getLocalPort();
    }
    public InetAddress getAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() {
//        TODO
        socket.close();
    }

    static class ServerListener implements Runnable {
        private ClientPlayer clientPlayer;

        public ServerListener(ClientPlayer clientPlayer) {
            this.clientPlayer = clientPlayer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    DatagramPacket packet = new DatagramPacket(clientPlayer.buffer, clientPlayer.buffer.length);
                    clientPlayer.socket.receive(packet);
                    byte[] data = packet.getData();
                    clientPlayer.parseMessage(data);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


    }

}
