package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;

public class JoinController {
    @FXML
    private Button join;

    @FXML
    private TextField code;
    private final FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("/fxml/game-view.fxml"));
    @FXML
    protected void onJoinButtonClick() throws IOException {
        String room = code.getText();
        Stage stage = (Stage) join.getScene().getWindow();
        ControllerHelper.loadAndShowFXML(fxmlLoader,  stage);
        ControllerHelper.getApplication().initClientPlayer(room);
    }
}