package ru.kpfu.itis.oris.gimaletdinova.client;

import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.exceptions.RoomNotFoundException;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

import static ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType.*;
import static ru.kpfu.itis.oris.gimaletdinova.server.GameServer.BUFFER_LENGTH;

public class ClientPlayer implements Closeable {
    private final GameApplication application;
    private final DatagramSocket socket;
    private final byte[] buffer = new byte[BUFFER_LENGTH];
    private final InetAddress address;
    private final Integer port;
    private final Queue<Message> serverMessages = new LinkedList<>();

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
        if (data[0] == CONNECT.getValue()) {
            ConnectResponseMessage message = new ConnectResponseMessage(data);
            application.getUser().setPosition(message.getPosition());
        }
        if (data[0] == START_GAME.getValue()) {
            GameStartMessage message = new GameStartMessage(data);
            application.startGame(message.getUsers(), message.getCharacters());
        }
        if (data[0] == MOVE.getValue()) {
            MoveMessage moveMessage = new MoveMessage(data);
            serverMessages.add(moveMessage);
        }
        if (data[0] == ADD_BOMB.getValue()) {
            AddBombMessage addBombMessage = new AddBombMessage(data);
            serverMessages.add(addBombMessage);
        }
        if (data[0] == LOSE.getValue()) {
            LoseMessage loseMessage = new LoseMessage(data);
            serverMessages.add(loseMessage);
        }
    }

    public Queue<Message> getServerMessages() {
        return serverMessages;
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
        socket.close();
    }

    private static class ServerListener implements Runnable {
        private final ClientPlayer clientPlayer;

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
