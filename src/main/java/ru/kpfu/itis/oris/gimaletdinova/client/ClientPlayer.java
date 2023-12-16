package ru.kpfu.itis.oris.gimaletdinova.client;

import javafx.application.Platform;
import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.exceptions.RoomNotFoundException;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.*;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.*;
import java.net.*;

import static ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType.*;
import static ru.kpfu.itis.oris.gimaletdinova.server.GameServer.BUFFER_LENGTH;

public class ClientPlayer implements Closeable {
    private final GameApplication application;
    private final DatagramSocket socket;
    private final byte[] buffer = new byte[BUFFER_LENGTH];
    private final InetAddress address;
    private final Integer port;
    private boolean isAlive = true;
    private MoveMessage moveMessage;
    private AddBombMessage addBombMessage;
    private LoseMessage loseMessage;
    private UserJoinMessage userJoinMessage;

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
            System.out.println(message.getPosition());
            application.getUser().setPosition(message.getPosition());
            return;
        }
        if (data[0] == JOIN_USER.getValue()) {
            userJoinMessage = new UserJoinMessage(data);
            System.out.println(userJoinMessage.getUsername());
            return;
//            Platform.runLater(this::addUser);
        }
        if (data[0] == CLOSE_ROOM.getValue()) {
            close();
            Platform.runLater(this::exit);
            return;
        }
        if (data[0] == START_GAME.getValue()) {
            GameStartMessage message = new GameStartMessage(data);
            application.startGame(message.getUsers(), message.getCharacters());
            return;
        }
        if (data[0] == MOVE.getValue()) {
            moveMessage = new MoveMessage(data);
            Platform.runLater(this::move);
            return;
        }
        if (data[0] == ADD_BOMB.getValue()) {
            addBombMessage = new AddBombMessage(data);
            Platform.runLater(this::addBomb);
            return;
        }
        if (data[0] == LOSE.getValue()) {
            loseMessage = new LoseMessage(data);
            Platform.runLater(this::lose);
        }
    }

    private void addUser() {
        application.gameWaitingViewController.addPlayer(userJoinMessage.getUsername());
    }

    private void exit() {
        try {
            ApplicationUtil.loadAndShowFXML("/fxml/start-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void move() {
        ApplicationUtil.getApplication().gameController.moveCharacter(moveMessage.getPlayerPosition(), moveMessage.getCode());
    }

    private void addBomb() {
        ApplicationUtil.getApplication().gameController.addBomb(addBombMessage.getX(), addBombMessage.getY());
    }

    private void lose() {
        ApplicationUtil.getApplication().gameController.removeCharacter(loseMessage.getPlayerPosition());
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
        isAlive = false;
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        socket.close();
    }


    private class ServerListener implements Runnable {
        private final ClientPlayer clientPlayer;

        public ServerListener(ClientPlayer clientPlayer) {
            this.clientPlayer = clientPlayer;
        }

        @Override
        public void run() {
            while (isAlive) {
                try {
                    DatagramPacket packet = new DatagramPacket(clientPlayer.buffer, clientPlayer.buffer.length);
                    clientPlayer.socket.receive(packet);
                    byte[] data = packet.getData();
                    clientPlayer.parseMessage(data);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
