package ru.kpfu.itis.oris.gimaletdinova;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.kpfu.itis.oris.gimaletdinova.client.ClientPlayer;
import ru.kpfu.itis.oris.gimaletdinova.controller.GameWaitingViewController;
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
    private ClientPlayer clientPlayer;
    private User user;
    private String room;
    private int[] characters;
    private String[] users;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setOnCloseRequest(e -> {
            Map<String, Object> map = new HashMap<>();
            DisconnectMessage message = new DisconnectMessage(map);
            clientPlayer.send(message);
            clientPlayer.close();
            System.exit(0);
        });

        ControllerHelper.setApplication(this);

        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/fxml/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 525);

        stage.initStyle(StageStyle.UNDECORATED);
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
        GameWaitingViewController.isPlayersReady = true;
    }

    public int[] getCharacters() {
        return characters;
    }

    public String[] getUsers() {
        return users;
    }
}