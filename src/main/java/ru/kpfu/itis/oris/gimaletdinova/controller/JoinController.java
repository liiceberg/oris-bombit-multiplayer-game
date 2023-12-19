package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.IOException;
import java.sql.SQLException;

import static ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil.getApplication;

public class JoinController implements Controller {
    @FXML
    public Label error;
    @FXML
    private TextField code;
    @FXML
    private Button join;
    @FXML
    private Button cancel;
    private final EventHandler<ActionEvent> joinHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == join) {
                onJoinButtonClick();
            }
        }
    };
    private final EventHandler<ActionEvent> cancelHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == cancel) {
                try {
                    ApplicationUtil.loadAndShowFXML("/fxml/start-view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    @FXML
    public void initialize() {
        join.setOnAction(joinHandler);
        cancel.setOnAction(cancelHandler);
    }

    @FXML
    protected void onJoinButtonClick() {
        error.setVisible(false);
        String room = code.getText();
        try {
            if (RoomRepository.dao.isRoomExist(room)) {
                getApplication().initClientPlayer(room);
                getApplication().waitPlayers();

            } else {
                error.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}