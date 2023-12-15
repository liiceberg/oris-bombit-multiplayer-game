package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.model.message.DisconnectMessage;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;
import ru.kpfu.itis.oris.gimaletdinova.util.GameFieldRepository;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper.getApplication;

public class GameOverController {
    @FXML
    public Label title;
    private final String WIN_TEXT = "You win!!";
    private final String LOSE_TEXT = "Ooops... You lose((";
    @FXML
    public Button playButton;

    FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/fxml/start-view.fxml"));

    @FXML
    public void initialize() {
        Platform.runLater(this::init);
    }
    private void init() {
        if (getApplication().isWin) {
            title.setText(WIN_TEXT);
        } else {
            title.setText(LOSE_TEXT);
        }
        if (getApplication().getUser().getPosition() != 1) {
            playButton.setVisible(false);
        }
    }

    public void onPlayButtonClicked() {
        try {
            RoomRepository.dao.updateField(getApplication().getRoom(), GameFieldRepository.getGameField());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getApplication().gameController = new GameController();
        getApplication().startGame();
    }

    public void onExitButtonClicked() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("position", getApplication().getUser().getPosition());
            DisconnectMessage message = new DisconnectMessage(map);
            getApplication().getClientPlayer().send(message);
            getApplication().getClientPlayer().close();
            ControllerHelper.loadAndShowFXML(fxmlLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
