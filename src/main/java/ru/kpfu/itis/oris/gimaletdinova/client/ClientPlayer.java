package ru.kpfu.itis.oris.gimaletdinova.client;

import javafx.application.Platform;
import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.*;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;
import ru.kpfu.itis.oris.gimaletdinova.util.GameFieldRepository;
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
    private boolean isAlive = true;
    private final Queue<MoveMessage> moveMessages = new LinkedList<>();
    private final Queue<AddBombMessage> addBombMessages = new LinkedList<>();
    private final Queue<LoseMessage> loseMessages = new LinkedList<>();

    public ClientPlayer(GameApplication application, String room) {
        this.application = application;
        port = RoomRepository.getPort(room);
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
        } catch (IOException ignored) {
        }
    }

    private void parseMessage(byte[] data) throws IOException, ClassNotFoundException {
        if (data[0] == CONNECT.getValue()) {
            ConnectResponseMessage message = new ConnectResponseMessage(data);
            application.getUser().setPosition(message.getPosition());
            return;
        }
        if (data[0] == JOIN_USER.getValue()) {
            UserJoinMessage userJoinMessage = new UserJoinMessage(data);
            application.getUsers().add(userJoinMessage.getUsername());
            Platform.runLater(this::updatePlayersList);
            return;
        }
        if (data[0] == DISCONNECT.getValue()) {
            DisconnectMessage disconnectMessage = new DisconnectMessage(data);
            application.getUsers().remove(disconnectMessage.getPlayerPosition() - 1);
            Platform.runLater(this::updatePlayersList);
            return;
        }
        if (data[0] == CLOSE_ROOM.getValue()) {
            application.getUsers().clear();
            Platform.runLater(this::exit);
            return;
        }
        if (data[0] == GAME_FIELD.getValue()) {
            GameFieldMessage gameFieldMessage = new GameFieldMessage(data);
            application.setGameFiled(
                    GameFieldRepository.getGameField(
                            gameFieldMessage.getFieldMode(),
                            gameFieldMessage.getObstacles()
                    )
            );
            application.setMode(gameFieldMessage.getObstaclesMode());

        }
        if (data[0] == START_GAME.getValue()) {
            GameStartMessage message = new GameStartMessage(data);
            application.startGame(message.getCharacters());
            return;
        }
        if (data[0] == MOVE.getValue()) {
            moveMessages.add(new MoveMessage(data));
            Platform.runLater(this::move);
            return;
        }
        if (data[0] == ADD_BOMB.getValue()) {
            addBombMessages.add(new AddBombMessage(data));
            Platform.runLater(this::addBomb);
            return;
        }
        if (data[0] == LOSE.getValue()) {
            loseMessages.add(new LoseMessage(data));
            Platform.runLater(this::lose);
            return;
        }
        if (data[0] == PLAY_AGAIN.getValue()) {
            Platform.runLater(this::playAgain);
        }
        if (data[0] == FULL_ROOM.getValue()) {
            application.isRoomFull = true;
            application.getUser().setPosition(-1);
        }
    }

    private void updatePlayersList() {
        application.gameWaitingViewController.updatePlayers();
    }

    private void exit() {
        try {
            close();
            ApplicationUtil.loadAndShowFXML("/fxml/start-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void playAgain() {
        application.waitPlayers();
        application.gameWaitingViewController.updatePlayers();
    }

    private void move() {
        MoveMessage m = moveMessages.poll();
        if (m != null) {
            ApplicationUtil.getApplication().gameController.moveCharacter(m.getPlayerPosition(), m.getCode());
        }
    }

    private void addBomb() {
        AddBombMessage m = addBombMessages.poll();
        if (m != null) {
            ApplicationUtil.getApplication().gameController.addBomb(m.getX(), m.getY());
        }
    }

    private void lose() {
        LoseMessage m = loseMessages.poll();
        if (m != null) {
            ApplicationUtil.getApplication().gameController.removeCharacter(m.getPlayerPosition());
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
        isAlive = false;
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
                    return;
                }
            }
        }
    }

}
