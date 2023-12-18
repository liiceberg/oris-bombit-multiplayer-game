package ru.kpfu.itis.oris.gimaletdinova;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.client.ClientPlayer;
import ru.kpfu.itis.oris.gimaletdinova.controller.GameController;
import ru.kpfu.itis.oris.gimaletdinova.controller.GameWaitingViewController;
import ru.kpfu.itis.oris.gimaletdinova.controller.JoinController;
import ru.kpfu.itis.oris.gimaletdinova.model.Block;
import ru.kpfu.itis.oris.gimaletdinova.model.Mode;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.model.User;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.ConnectMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.DisconnectMessage;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil.getApplication;

public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    public GameController gameController;
    public GameWaitingViewController gameWaitingViewController;
    public JoinController joinController;
    private ClientPlayer clientPlayer;
    private User user;
    private String room;
    private int[] characters;
    public List<String> usersList = new ArrayList<>();
    private Block[][] gameFiled;
    private Mode mode;
    public boolean isWin;

    @Override
    public void start(Stage stage) throws IOException {
        ApplicationUtil.setPrimaryStage(stage);

        stage.setOnCloseRequest(e -> {
            if (clientPlayer != null) {
                DisconnectMessage message = new DisconnectMessage(user.getPosition());
                clientPlayer.send(message);
                clientPlayer.close();
            }
            System.exit(0);
        });

        ApplicationUtil.setApplication(this);

//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("BOMB IT");
        ApplicationUtil.loadAndShowFXML("/fxml/start-view.fxml");


    }

    public ClientPlayer getClientPlayer() {
        return clientPlayer;
    }

    public boolean initClientPlayer(String room) {
        clientPlayer = new ClientPlayer(this, room);
        Message message = new ConnectMessage(user.getUsername(), clientPlayer.getPort(), clientPlayer.getAddress());
        clientPlayer.send(message);
        this.room = room;
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoom() {
        return room;
    }

    public void startGame(int[] characters) {
        this.characters = characters;
        Platform.runLater(this::startGame);
    }

    public void startGame() {
        try {
            isWin = true;
            gameController = new GameController();
            ApplicationUtil.loadAndShowFXML("/fxml/game-view.fxml", gameController);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitPlayers() {
        gameWaitingViewController = new GameWaitingViewController();
        try {
            ApplicationUtil.loadAndShowFXML("/fxml/game-waiting-view.fxml", gameWaitingViewController);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void join() {
        try {
            joinController = new JoinController();
            ApplicationUtil.loadAndShowFXML("/fxml/join-view.fxml", joinController);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void exit() {
        DisconnectMessage message = new DisconnectMessage(getApplication().getUser().getPosition());
        clientPlayer.send(message);
        clientPlayer.close();
        clientPlayer = null;
    }

    public int[] getCharacters() {
        return characters;
    }

    public List<String> getUsers() {
        return usersList;
    }

    public Block[][] getGameFiled() {
        return gameFiled;
    }

    public void setGameFiled(Block[][] gameFiled) {
        this.gameFiled = gameFiled;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}