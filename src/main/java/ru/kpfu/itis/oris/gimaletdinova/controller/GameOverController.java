package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.DisconnectMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.PlayAgainMessage;
import ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil;
import ru.kpfu.itis.oris.gimaletdinova.util.GameFieldRepository;
import ru.kpfu.itis.oris.gimaletdinova.util.RoomRepository;

import java.io.IOException;
import java.sql.SQLException;

import static ru.kpfu.itis.oris.gimaletdinova.util.ApplicationUtil.getApplication;

public class GameOverController implements Controller {
    @FXML
    public Label title;
    private final String WIN_TEXT = "You win!!";
    private final String LOSE_TEXT = "Ooops... You lose((";
    @FXML
    public Button playButton;

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
        PlayAgainMessage playAgainMessage = new PlayAgainMessage();
        getApplication().getClientPlayer().send(playAgainMessage);
    }

    public void onExitButtonClicked() {
        try {
            ApplicationUtil.getApplication().exit();
            ApplicationUtil.loadAndShowFXML("/fxml/start-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
