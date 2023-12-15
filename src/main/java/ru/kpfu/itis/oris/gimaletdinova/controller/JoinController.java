package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

import java.io.IOException;

public class JoinController {
    @FXML
    public Label error;

    @FXML
    private TextField code;
    private final FXMLLoader fxmlLoader = new FXMLLoader(GameWaitingViewController.class.getResource("/fxml/game-waiting-view.fxml"));
    @FXML
    protected void onJoinButtonClick() throws IOException {
        String room = code.getText();
        if (!ControllerHelper.getApplication().initClientPlayer(room)) {
            error.setVisible(true);
        } else {
            ControllerHelper.loadAndShowFXML(fxmlLoader);
        }
    }
}