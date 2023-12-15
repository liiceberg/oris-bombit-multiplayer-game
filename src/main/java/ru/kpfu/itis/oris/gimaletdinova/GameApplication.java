package ru.kpfu.itis.oris.gimaletdinova;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.client.ClientPlayer;
import ru.kpfu.itis.oris.gimaletdinova.controller.GameController;
import ru.kpfu.itis.oris.gimaletdinova.exceptions.RoomNotFoundException;
import ru.kpfu.itis.oris.gimaletdinova.model.message.*;
import ru.kpfu.itis.oris.gimaletdinova.model.User;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    public GameController gameController = new GameController();
    public Stage primaryStage;
    private ClientPlayer clientPlayer;
    private User user;
    private String room;
    private int[] characters;
    private String[] users;
    public boolean isWin = true;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        stage.setOnCloseRequest(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("position", user.getPosition());
            DisconnectMessage message = new DisconnectMessage(map);
            clientPlayer.send(message);
            clientPlayer.close();
            System.exit(0);
        });

        ControllerHelper.setApplication(this);

        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/fxml/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 525);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("BOMB IT");
        stage.setScene(scene);
        stage.show();

    }

    public ClientPlayer getClientPlayer() {
        return clientPlayer;
    }

    public boolean initClientPlayer(String room) {
        try {
            clientPlayer = new ClientPlayer(this, room);
        } catch (RoomNotFoundException e) {
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("port", clientPlayer.getPort());
        map.put("address", clientPlayer.getAddress());
        Message message = new ConnectMessage(map);
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
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/fxml/game-view.fxml"));
            fxmlLoader.setController(gameController);
            Scene s = new Scene(fxmlLoader.load(), 750, 525);
            primaryStage.setScene(s);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getCharacters() {
        return characters;
    }

    public String[] getUsers() {
        return users;
    }

}