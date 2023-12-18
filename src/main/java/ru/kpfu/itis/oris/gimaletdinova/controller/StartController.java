package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.kpfu.itis.oris.gimaletdinova.model.User;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.IOException;

public class StartController implements Controller {
    @FXML
    public Label input_error;

    @FXML
    private TextField username;

    @FXML
    protected void onJoinButtonClick() {
        if (validateUsername()) {
            ApplicationUtil.getApplication().join();
        }
    }

    @FXML
    protected void onCreateButtonClick() {
        if (validateUsername()) {
            String room = RoomRepository.createRoom();

            ApplicationUtil.getApplication().initClientPlayer(room);

            ApplicationUtil.getApplication().waitPlayers();
        }
    }

    private boolean validateUsername() {
        String name = username.getText();
        if (3 <= name.length() && name.length() <= 15) {
            User user = new User(name);
            ApplicationUtil.getApplication().setUser(user);
            return true;
        }
        input_error.setVisible(true);
        return false;
    }

}
