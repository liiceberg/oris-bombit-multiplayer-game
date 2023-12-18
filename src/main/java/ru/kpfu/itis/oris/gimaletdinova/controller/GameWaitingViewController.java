package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;


public class GameWaitingViewController implements Controller {
    @FXML
    public Label code;
    @FXML
    public VBox usersBox;

    @FXML
    public void initialize() {
        code.setText(ApplicationUtil.getApplication().getRoom());
    }

    public void updatePlayers() {
        usersBox.getChildren().clear();
        for (String name: ApplicationUtil.getApplication().usersList) {
            Label label = new Label(name);
            usersBox.getChildren().add(label);
        }
    }

}
