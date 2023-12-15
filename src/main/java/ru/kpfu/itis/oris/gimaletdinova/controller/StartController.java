package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.model.User;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.IOException;

public class StartController {
    @FXML
    public Label input_error;

    @FXML
    private Button create;

    @FXML
    private Button join;

    @FXML
    private TextField username;

    private final FXMLLoader joinFxmlLoader = new FXMLLoader(JoinController.class.getResource("/fxml/join-view.fxml"));
    private final FXMLLoader createFxmlLoader = new FXMLLoader(GameWaitingViewController.class.getResource("/fxml/game-waiting-view.fxml"));

    @FXML
    protected void onJoinButtonClick() throws IOException {
        if (validateUsername()) {
            ControllerHelper.loadAndShowFXML(joinFxmlLoader);
        }
    }

    @FXML
    protected void onCreateButtonClick() throws IOException {
        if (validateUsername()) {
            String room = RoomRepository.createRoom();

            ControllerHelper.getApplication().initClientPlayer(room);

            ControllerHelper.loadAndShowFXML(createFxmlLoader);
        }
    }

    private boolean validateUsername() {
        String name = username.getText();
        if (3 <= name.length() && name.length() <= 15) {
            User user = new User(name);
            ControllerHelper.getApplication().setUser(user);
            return true;
        }
        input_error.setVisible(true);
        return false;
    }

}
