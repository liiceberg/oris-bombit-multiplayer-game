package ru.kpfu.itis.oris.gimaletdinova;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.kpfu.itis.oris.gimaletdinova.client.ClientPlayer;
import ru.kpfu.itis.oris.gimaletdinova.model.Player;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;
import java.util.Objects;

public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    private ClientPlayer clientPlayer;
    private Player player;

    @Override
    public void start(Stage stage) throws IOException {
        ClientPlayer clientPlayer = new ClientPlayer(this);
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

}