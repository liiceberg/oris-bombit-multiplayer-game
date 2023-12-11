package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;

public class GameWaitingViewController {
    @FXML
    public Label code;
    private final FXMLLoader fxmlLoader = new FXMLLoader(GameWaitingViewController.class.getResource("/fxml/game-view.fxml"));
    public static boolean isPlayersReady = false;
    @FXML
    public void initialize() throws IOException {
        code.setText(ControllerHelper.getApplication().getRoom());
        while (true) {
            if (isPlayersReady) {
                Stage stage = (Stage) code.getScene().getWindow();
                ControllerHelper.loadAndShowFXML(fxmlLoader,  stage);
            }
        }
    }


}
