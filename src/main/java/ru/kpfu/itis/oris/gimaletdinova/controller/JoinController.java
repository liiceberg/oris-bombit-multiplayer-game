package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;

import java.io.IOException;

public class JoinController implements Controller {
    @FXML
    public Label error;

    @FXML
    private TextField code;
    @FXML
    protected void onJoinButtonClick() {
        String room = code.getText();
        if (!ApplicationUtil.getApplication().initClientPlayer(room)) {
            error.setVisible(true);
        } else {
            ApplicationUtil.getApplication().waitPlayers();
        }
    }
}