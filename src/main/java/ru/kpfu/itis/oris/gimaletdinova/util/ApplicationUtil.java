package ru.kpfu.itis.oris.gimaletdinova.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kpfu.itis.oris.gimaletdinova.GameApplication;
import ru.kpfu.itis.oris.gimaletdinova.controller.Controller;

import java.io.IOException;

public class ApplicationUtil {
    private static GameApplication gameApplication;
    private static Stage primaryStage;
    public static void loadAndShowFXML(String resource) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource(resource));
        loadAndShowFXML(fxmlLoader);
    }

    public static void loadAndShowFXML(String resource, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource(resource));
        fxmlLoader.setController(controller);
        loadAndShowFXML(fxmlLoader);
    }

    private static void loadAndShowFXML(FXMLLoader fxmlLoader) throws IOException {
        Scene s = new Scene(fxmlLoader.load(), 750, 525);
        primaryStage.setScene(s);
        primaryStage.show();
    }


    public static GameApplication getApplication() {
        return gameApplication;
    }

    public static void setApplication(GameApplication application) {
        gameApplication = application;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        ApplicationUtil.primaryStage = primaryStage;
    }
}
