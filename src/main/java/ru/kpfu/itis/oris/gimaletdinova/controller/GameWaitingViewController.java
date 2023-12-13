package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;

public class GameWaitingViewController {
    @FXML
    public Label code;
    private final FXMLLoader fxmlLoader = new FXMLLoader(GameWaitingViewController.class.getResource("/fxml/game-view.fxml"));
    public static boolean isPlayersReady = false;
    @FXML
    public VBox usersBox;

    @FXML
    public void initialize() {
        code.setText(ControllerHelper.getApplication().getRoom());
        Platform.runLater(new Listener());

    }

    private void start() {
        Stage stage = (Stage) code.getScene().getWindow();
        try {
            ControllerHelper.loadAndShowFXML(fxmlLoader,  stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class Listener implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (isPlayersReady) {
                    start();
                    break;
                }
            }
        }
    }

}
