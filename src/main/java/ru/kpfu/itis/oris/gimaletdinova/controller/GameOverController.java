package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;

public class GameOverController {
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
        if (ControllerHelper.getApplication().isWin) {
            title.setText(WIN_TEXT);
        } else {
            title.setText(LOSE_TEXT);
        }
        if (ControllerHelper.getApplication().getUser().getPosition() != 0) {
            playButton.setVisible(false);
        }
    }

    public void onPlayButtonClicked() {

    }

    public void onExitButtonClicked() {

    }
}
