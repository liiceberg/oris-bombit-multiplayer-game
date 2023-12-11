package ru.kpfu.itis.oris.gimaletdinova.server;

import ru.kpfu.itis.oris.gimaletdinova.dao.Dao;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.util.CharacterFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.*;

import static ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType.*;

public class GameServer implements Closeable, Runnable {
    private final int port;
    public static final int BUFFER_LENGTH = 200;
    private final byte[] buffer = new byte[BUFFER_LENGTH];
    private final DatagramSocket socket;
    private final List<Client> players = new ArrayList<>();
    public static final int PLAYERS_COUNT = 2;
    private final InetAddress address;

    public GameServer() {
        try {
            socket = new DatagramSocket();
            port = socket.getLocalPort();
            address = InetAddress.getLocalHost();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                parseMessage(packet.getData());

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void parseMessage(byte[] data) throws IOException, ClassNotFoundException {
        if (data[0] == CONNECT.getValue()) {
            addPlayer(new ConnectMessage(data));
        }
        if (data[0] == DISCONNECT.getValue()) {
            close();
        }
    }

    private void addPlayer(ConnectMessage message) {
        if (players.size() + 1 <= PLAYERS_COUNT) {
            Client client = new Client(this, message.getAddress(), message.getPort(), message.getUsername());
            players.add(client);
            new Thread(client).start();
            if (players.size() == PLAYERS_COUNT) {
                Map<String, Object> map = new HashMap<>();
                String[] users = new String[PLAYERS_COUNT];
                int[] characters = new int[PLAYERS_COUNT];
                for (int i = 0; i < players.size(); i++) {
                    users[i] = players.get(i).username;
                    characters[i] = players.get(i).characterImg;
                }
                map.put("users", users);
                map.put("characters", characters);
                GameStartMessage gameStartMessage = new GameStartMessage(map);
                sendAll(gameStartMessage);
            }
        }
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public void sendMessage(Message message, Client client) {
        try {
            byte[] data = message.getByteContent();
            DatagramPacket packet = new DatagramPacket(data, data.length, client.clientAddress, client.clientPort);
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAll(Message message) {
        for (Client client: players) {
            sendMessage(message, client);
        }
    }

    @Override
    public void close() {
        socket.close();
//        TODO don't create new dao
        Dao dao = new Dao();
        try {
            dao.delete(port);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static class Client implements Runnable {
        private final GameServer server;
        private InetAddress clientAddress;
        private int clientPort;
        private String username;
        private int characterImg;
        public Client(GameServer gameServer, InetAddress address, int port, String username) {
            clientAddress = address;
            clientPort = port;
            server = gameServer;
            this.username = username;
            characterImg = CharacterFactory.getNumber();
        }

        @Override
        public void run() {
            System.out.println("Player added");
//            Message m = new ConnectMessage();
//            server.sendMessage(m, this);
//            System.out.println("message from server " + m);
//            try {
//                while (true) {
//                    DatagramPacket packet = new DatagramPacket(server.buffer, server.buffer.length);
//                    server.socket.receive(packet);
//                    server.parseMessage(packet.getData());
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
        }


    }

}
