package example._mydictionary;

import example._mydictionary.AutoCompleteComboBox;
import javafx.beans.property.SetProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class HelloController implements Initializable {
    @FXML
    ComboBox<String> searchComboBox = new ComboBox<>();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextArea outputText;


    // Switch to HomePage when click Home button.
    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Switch to CreatePage when click Create button.
    public void switchToCreate(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("create-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Switch to EditPage when click Edit button.
    public void switchToEdit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    // Speak words in search bar.
    public void voice(ActionEvent event){
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            try {
                voice.setRate(170);
                voice.setPitch(100);
                voice.setVolume(200);
                voice.speak(searchComboBox.getEditor().getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Can't find voice: kevin16");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBController.init();
        DBController.printHis();
        searchComboBox.getItems().addAll(DBController.words);
        AutoCompleteComboBox.autoCompleteComboBox(searchComboBox, AutoCompleteComboBox.AutoCompleteMode.STARTS_WITH);
        ComboBoxListViewSkin skin = new ComboBoxListViewSkin<>(searchComboBox);
        skin.getPopupContent().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.SPACE) {
                e.consume();
            }
        });
        searchComboBox.setSkin(skin);
        searchComboBox.getEditor().textProperty().addListener(((observableValue, s, t1) -> {
            String input_word = searchComboBox.getEditor().getText();
            if (DBController.words.contains(input_word)) {
                outputText.setText(DBController.dictData.get(input_word));
            }
            if (input_word.length() == 0) {
                outputText.setText("");
            }
        }));
    }
}