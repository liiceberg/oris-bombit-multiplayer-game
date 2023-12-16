package ru.kpfu.itis.oris.gimaletdinova;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.client.ClientPlayer;
import ru.kpfu.itis.oris.gimaletdinova.controller.GameController;
import ru.kpfu.itis.oris.gimaletdinova.controller.GameWaitingViewController;
import ru.kpfu.itis.oris.gimaletdinova.exceptions.RoomNotFoundException;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.model.User;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.ConnectMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.DisconnectMessage;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;

import java.io.IOException;

import static ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil.getApplication;

public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    public GameController gameController;
    public GameWaitingViewController gameWaitingViewController;
    private ClientPlayer clientPlayer;
    private User user;
    private String room;
    private int[] characters;
    private String[] users;
    public boolean isWin = true;

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
        gameWaitingViewController = new GameWaitingViewController();
        try {
            clientPlayer = new ClientPlayer(this, room);
        } catch (RoomNotFoundException e) {
            return false;
        }
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

    public void startGame(String[] users, int[] characters) {
        this.characters = characters;
        this.users = users;
        Platform.runLater(this::startGame);
    }

    public void startGame() {
        try {
            gameController = new GameController();
            ApplicationUtil.loadAndShowFXML("/fxml/game-view.fxml", gameController);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitPlayers() {
        try {
            ApplicationUtil.loadAndShowFXML("/fxml/game-waiting-view.fxml", gameWaitingViewController);
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

    public String[] getUsers() {
        return users;
    }

}