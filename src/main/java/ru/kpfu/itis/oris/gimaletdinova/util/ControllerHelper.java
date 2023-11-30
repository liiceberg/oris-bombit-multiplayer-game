package ru.kpfu.itis.oris.gimaletdinova.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.GameApplication;

import java.io.IOException;

public class ControllerHelper {
    private static GameApplication gameApplication;
    public static void loadAndShowFXML(FXMLLoader fxmlLoader, Stage stage) throws IOException {
        stage.setScene(new Scene(fxmlLoader.load(), 750, 525));
        stage.show();
    }

    public static GameApplication getApplication() {
        return gameApplication;
    }

    public static void setApplication(GameApplication application) {
        gameApplication = application;
    }
}
