package ru.kpfu.itis.oris.gimaletdinova;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.kpfu.itis.oris.gimaletdinova.client.ClientPlayer;
import ru.kpfu.itis.oris.gimaletdinova.model.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.MessageType;
import ru.kpfu.itis.oris.gimaletdinova.model.User;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;
public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    private ClientPlayer clientPlayer;
    private User user;
    private String room;

    @Override
    public void start(Stage stage) throws IOException {
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

    public void initClientPlayer(String room) {
        clientPlayer = new ClientPlayer(this, room);
        Message message = new Message(MessageType.CONNECT, user.getUsername());
        clientPlayer.send(message);
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

    public void setRoom(String room) {
        this.room = room;
    }
}