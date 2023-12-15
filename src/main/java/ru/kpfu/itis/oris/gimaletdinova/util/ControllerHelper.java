package ru.kpfu.itis.oris.gimaletdinova.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ru.kpfu.itis.oris.gimaletdinova.GameApplication;

import java.io.IOException;

public class ControllerHelper {
    private static GameApplication gameApplication;
    public static void loadAndShowFXML(FXMLLoader fxmlLoader) throws IOException {
        Scene s = new Scene(fxmlLoader.load(), 750, 525);
        gameApplication.primaryStage.setScene(s);
        gameApplication.primaryStage.show();
    }

    public static GameApplication getApplication() {
        return gameApplication;
    }

    public static void setApplication(GameApplication application) {
        gameApplication = application;
    }
}
