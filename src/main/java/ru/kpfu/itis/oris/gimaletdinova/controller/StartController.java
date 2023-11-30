package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.model.Player;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;

public class StartController {
    @FXML
    public TextField username;

    @FXML
    private Button start;
    private final FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("/fxml/game-view.fxml"));
    @FXML
    protected void onStartButtonClick() throws IOException {
        new Player();
        Stage stage = (Stage) start.getScene().getWindow();
        ControllerHelper.loadAndShowFXML(fxmlLoader,  stage);
//        ControllerHelper.getApplication().
    }
}