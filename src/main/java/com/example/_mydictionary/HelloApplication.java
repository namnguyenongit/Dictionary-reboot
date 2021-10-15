package example._mydictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("src/main/resources/com/example/_mydictionary/home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styles.css")).toExternalForm());
//        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600&display=swap");
        stage.setTitle("What A Dict!");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() throws SQLException {
        System.out.println("Stage is closing");
        DBController.ResetOnClose();
        DBController.UpdateOnClose();
    }

    public static void main(String[] args) {
        launch();
    }
}