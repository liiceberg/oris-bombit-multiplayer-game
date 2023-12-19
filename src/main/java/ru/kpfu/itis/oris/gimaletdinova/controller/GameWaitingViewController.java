package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;

import java.io.IOException;


public class GameWaitingViewController implements Controller {
    @FXML
    public Label code;
    @FXML
    public Label main;
    @FXML
    public Label roomFullError;
    @FXML
    public VBox usersBox;
    @FXML
    public Button exit;
    private final EventHandler<ActionEvent> exitHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == exit) {
                try {
                    ApplicationUtil.getApplication().exit();
                    ApplicationUtil.loadAndShowFXML("/fxml/start-view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    @FXML
    public void initialize() {
        code.setText(ApplicationUtil.getApplication().getRoom());
        if (ApplicationUtil.getApplication().isRoomFull) showError();
        exit.setOnAction(exitHandler);
    }

    public void updatePlayers() {
        usersBox.getChildren().clear();
        for (String name: ApplicationUtil.getApplication().getUsers()) {
            Label label = new Label(name);
            label.setStyle("-fx-text-fill: grey");
            usersBox.getChildren().add(label);
        }
    }

    public void showError() {
        main.setVisible(false);
        roomFullError.setVisible(true);
    }

}
