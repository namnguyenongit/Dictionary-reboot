package com.example._mydictionary;

import example._mydictionary.AutoCompleteComboBox;
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
    private TextArea outputText;
    @FXML
    private Button removeButton;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Connect to DB to get word data.
        String dbUsername = "root";
        String dbPassword = "nvn120901";
        String dbURL = "jdbc:mysql://localhost:3307/dict";
        try {
            Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            ResultSet rs = connection.createStatement().executeQuery("SELECT word FROM tbl_dict_2c");
            while (rs.next()) {
                searchComboBox.getItems().addAll(rs.getString("word"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        // AutoCompleteComboBox declare.
        AutoCompleteComboBox.autoCompleteComboBox(searchComboBox, AutoCompleteComboBox.AutoCompleteMode.STARTS_WITH);

        // Consume when press SPACEBAR so the Editable ComboBox won't reset.
        ComboBoxListViewSkin skin = new ComboBoxListViewSkin<>(searchComboBox);
        skin.getPopupContent().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.SPACE) {
                e.consume();
            }
        });
        searchComboBox.setSkin(skin);

        // Set Text to TextArea whenever the Editable ComboBox changed.
        searchComboBox.getEditor().textProperty().addListener(((observableValue, s, t1) -> {
            String input_word = searchComboBox.getEditor().getText();
            try {
                Connection connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
                ResultSet rs = connection.createStatement().executeQuery("SELECT meaning FROM tbl_dict_2c WHERE word = '" + input_word + "'");
                while (rs.next()) {
                    outputText.setText(rs.getString("meaning"));
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
            if (Objects.equals(input_word, "")) {
                outputText.setText("");     // Case when input = null.
            }
        }));

        searchComboBox.getEditor().setOnMousePressed(event -> {
            System.out.println("MOUSE PRESSED!!!");
            // will add history here later.
        });

    }
}