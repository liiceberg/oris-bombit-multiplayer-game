package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.sql.SQLException;

public class JoinController implements Controller {
    public boolean isRoomFull = false;
    @FXML
    public Label error;
    @FXML
    public Label roomFullError;
    @FXML
    private TextField code;
    @FXML
    private Button join;
    private final EventHandler<ActionEvent> joinHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == join) {
                onJoinButtonClick();
            }
        }
    };

    @FXML
    public  void initialize() {
        join.setOnAction(joinHandler);
    }

    @FXML
    protected void onJoinButtonClick() {
        roomFullError.setVisible(false);
        error.setVisible(false);
        String room = code.getText();
        try {
            if (RoomRepository.dao.isRoomExist(room)) {
                if (!isRoomFull) {
                    ApplicationUtil.getApplication().initClientPlayer(room);
                    ApplicationUtil.getApplication().waitPlayers();
                } else {
                    roomFullError.setVisible(true);
                }

            } else {
                error.setVisible(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}