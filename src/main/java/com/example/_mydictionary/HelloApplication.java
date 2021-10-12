package com.example._mydictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styles.css")).toExternalForm());
        stage.setTitle("What A Dict!");
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/MyDict-logo.png")));
        stage.setScene(scene);
        stage.show();

        //Close logic
        stage.setOnCloseRequest(event -> {
            event.consume();
            logout(stage);
        });
    }
    //Close function
    public void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("You're about to logout?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
        if(alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Successfully logged out!");
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}