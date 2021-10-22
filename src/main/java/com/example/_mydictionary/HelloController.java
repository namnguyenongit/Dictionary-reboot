package com.example._mydictionary;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import example._mydictionary.AutoCompleteComboBox;
import example._mydictionary.DBController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    ComboBox<String> searchComboBox = new ComboBox<>();
    private Stage stage;
    private Scene scene;
    @FXML
    private TextArea searchTextArea;
    @FXML
    private TextField createTextField;
    @FXML
    private TextArea createTextArea;
    @FXML
    private TextField editTextField;
    @FXML
    private TextArea editTextArea;
    @FXML
    private Button editDeleteButton;

    public void editDelete(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        if (editTextField.getText().isEmpty() || editTextArea.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("Word or meaning is empty!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
            alert.showAndWait();
        } else {
            alert.setTitle("Delete confirmation!");
            alert.setContentText("You're about to delete this word!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
            if (alert.showAndWait().get() == ButtonType.OK) {
                DBController.delete(editTextField.getText());
                DBController.words.remove(editTextField.getText());
                DBController.dictData.remove(editTextField.getText());
                System.out.println("Successfully delete data!");
            }
        }
    }

    public void createSubmit(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        if (createTextField.getText().isEmpty() || createTextArea.getText().isEmpty()) {
            alert.setTitle("ERROR");
            alert.setContentText("Word or meaning is empty!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
            alert.showAndWait();
        } else if (!DBController.words.contains(createTextField.getText())) {
            DBController.add(createTextField.getText(), createTextArea.getText());
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("NOTIFICATION");
            alert.setContentText("Word added to library!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
            alert.showAndWait();
        } else {
            alert.setTitle("ERROR");
            alert.setContentText("Word already existed!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
            if (alert.showAndWait().get() == ButtonType.OK) {
                System.out.println("Word already existed!");
                createTextArea.setText(null);
                createTextField.setText(null);
            }
        }
    }


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
    public void voice(ActionEvent event) {
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

    public void editActionOnReleased(KeyEvent event) {
        String input_word = editTextField.getText();
        if (DBController.words.contains(input_word)) {
            editTextArea.setText(DBController.dictData.get(input_word));
            editDeleteButton.setDisable(false);
            editTextArea.setEditable(true);
        } else {
            editTextArea.setText("Dictionary doesn't have this word!");
            editTextArea.setEditable(false);
            editDeleteButton.setDisable(true);
        }
        if (input_word.length() == 0) {
            editTextArea.setText("");
        }
    }

    public void editSubmit(ActionEvent event) throws SQLException {
        String input_word = editTextField.getText();
        String newMeaning = editTextArea.getText();
        if (DBController.words.contains(input_word) && !DBController.dictData.get(input_word).equals(newMeaning)) {
            DBController.update(input_word, newMeaning);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            System.out.println("Submit fail");
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("Word or meaning is empty!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                searchTextArea.setText(DBController.dictData.get(input_word));
            }
            if (input_word.length() == 0) {
                searchTextArea.setText("");
            }
        }));
    }
}