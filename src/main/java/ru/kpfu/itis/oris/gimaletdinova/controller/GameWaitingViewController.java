package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.kpfu.itis.oris.gimaletdinova.util.ControllerHelper;


public class GameWaitingViewController {

    @FXML
    public Label code;
    @FXML
    public void initialize() {
        code.setText(ControllerHelper.getApplication().getRoom());
    }

}
