package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;

import java.util.ArrayList;
import java.util.List;


public class GameWaitingViewController implements Controller {
    @FXML
    public Label code;
    @FXML
    public VBox usersBox;
    private final List<Label> users = new ArrayList<>();
    private boolean isInitialized = false;

    @FXML
    public void initialize() {
        code.setText(ApplicationUtil.getApplication().getRoom());
        if (!isInitialized) {
            Platform.runLater(this::addAll);
        }
    }

    public void addAll() {
//        usersBox.getChildren().addAll(users);
    }

    public void addPlayer(String name) {
        Label label = new Label(name);
        users.add(label);
        if (usersBox != null) {
            isInitialized = true;
            usersBox.getChildren().add(label);
        }
    }

    public void removePlayer(int index) {
        usersBox.getChildren().remove(users.get(index));
        users.remove(index);
    }

}
