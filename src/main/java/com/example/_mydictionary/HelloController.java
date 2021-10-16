package com.example._mydictionary;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    ComboBox<String> searchComboBox = new ComboBox<>();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextArea searchTextArea;
    @FXML
    private Button removeButton;
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
        DBController.delete(editTextField.getText());
        DBController.words.remove(editTextField.getText());
        DBController.dictData.remove(editTextField.getText());
    }

    public void createSubmit(ActionEvent event) throws SQLException {
        if(createTextField.getText().isEmpty() || createTextArea.getText().isEmpty()){
            System.out.println("Word or Meaning is empty");
        } else if(!DBController.words.contains(createTextField.getText())){
            DBController.add(createTextField.getText(),createTextArea.getText());
        } else {
            System.out.println("Word already have");
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

    // Handle remove data
    public void handleRemove(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete confirmation!");
        alert.setHeaderText(null);
        alert.setContentText("You're about to delete this word!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("styles/dialogue.css").toExternalForm());
        if(alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Successfully delete data!");
            //Logic delete data here
        }
    }

    public void editActionOnReleased(KeyEvent event) {
        String input_word = editTextField.getText();
        if (DBController.words.contains(input_word)) {
            editTextArea.setText(DBController.dictData.get(input_word));
            editDeleteButton.setDisable(false);
        } else {
            editTextArea.setText("Dictionary doesn't have this word!");
            editTextArea.setEditable(false);
            editDeleteButton.setDisable(true);

        }
        if (input_word.length() == 0) {
            editTextArea.setText("");
        }
    }

    public void editSubmit(ActionEvent event) throws SQLException{
        String input_word = editTextField.getText();
        String newMeaning = editTextArea.getText();
        if (DBController.words.contains(input_word) && !DBController.dictData.get(input_word).equals(newMeaning)) {
            DBController.update(input_word, newMeaning);
        } else {
            System.out.println("Submit fail");
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