package ru.kpfu.itis.oris.gimaletdinova.server;

import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.*;
import ru.kpfu.itis.oris.gimaletdinova.util.CharacterFactory;
import ru.kpfu.itis.oris.gimaletdinova.util.GameFieldRepository;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

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
    public static final int PLAYERS_COUNT = 1;
    private final InetAddress address;
    private boolean isAlive = true;

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
            while (isAlive) {
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
            ConnectMessage connectMessage = new ConnectMessage(data);
            addPlayer(connectMessage);
            return;
        }
        if (data[0] == DISCONNECT.getValue()) {
            DisconnectMessage disconnectMessage = new DisconnectMessage(data);
            if (disconnectMessage.getPlayerPosition() == 1) {
                isAlive = false;
                RoomClosedMessage message = new RoomClosedMessage();
                sendAll(message);
                close();
            }
            return;
        }
        if (data[0] == MOVE.getValue()) {
            MoveMessage moveMessage = new MoveMessage(data);
            for (int i = 0; i < players.size(); i++) {
                if (moveMessage.getPlayerPosition() != i + 1) {
                    sendMessage(moveMessage, players.get(i));
                }
            }
            return;
        }
        if (data[0] == ADD_BOMB.getValue()) {
            AddBombMessage addBombMessage = new AddBombMessage(data);
            for (int i = 0; i < players.size(); i++) {
                if (addBombMessage.getPlayerPosition() != i + 1) {
                    sendMessage(addBombMessage, players.get(i));
                }
            }
            return;
        }
        if (data[0] == LOSE.getValue()) {
            LoseMessage loseMessage = new LoseMessage(data);
            for (int i = 0; i < players.size(); i++) {
                if (loseMessage.getPlayerPosition() != i + 1) {
                    sendMessage(loseMessage, players.get(i));
                }
            }
            return;
        }
        if (data[0] == PLAY_AGAIN.getValue()) {
            sendStartMessage();
        }
    }

    private void addPlayer(ConnectMessage message) {
        if (players.size() + 1 <= PLAYERS_COUNT) {
            Client client = new Client(message.getAddress(), message.getPort(), message.getUsername());
            sendCurrentUsers(client);
            players.add(client);
            ConnectResponseMessage responseMessage = new ConnectResponseMessage(players.size());
            sendMessage(responseMessage, client);
            UserJoinMessage userJoinMessage = new UserJoinMessage(client.username);
            sendAll(userJoinMessage);
            if (players.size() == PLAYERS_COUNT) {
                sendStartMessage();
            }
        }
    }
    private void sendCurrentUsers(Client client) {
        for (Client c: players) {
            UserJoinMessage userJoinMessage = new UserJoinMessage(c.username);
            sendMessage(userJoinMessage, client);
        }
    }
    private void sendStartMessage() {
        String[] users = new String[PLAYERS_COUNT];
        int[] characters = new int[PLAYERS_COUNT];
        for (int i = 0; i < players.size(); i++) {
            users[i] = players.get(i).username;
            characters[i] = players.get(i).characterImg;
        }
        GameFieldMessage gameFieldMessage = new GameFieldMessage(GameFieldRepository.generateGameField(), GameFieldRepository.getFieldMode(), GameFieldRepository.getObstaclesMode());
        sendAll(gameFieldMessage);
        GameStartMessage gameStartMessage = new GameStartMessage(users, characters);
        sendAll(gameStartMessage);
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
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
        for (Client client : players) {
            sendMessage(message, client);
        }
    }

    @Override
    public void close() {
        socket.close();
        try {
            RoomRepository.dao.delete(port);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Client {
        private final InetAddress clientAddress;
        private final int clientPort;
        private final String username;
        private final int characterImg;

        public Client(InetAddress address, int port, String username) {
            clientAddress = address;
            clientPort = port;
            this.username = username;
            characterImg = CharacterFactory.getNumber();
        }
    }

}
