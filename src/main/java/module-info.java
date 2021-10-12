module com.example._mydictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jsapi;
    requires freetts;


    opens com.example._mydictionary to javafx.fxml;
    exports com.example._mydictionary;
}