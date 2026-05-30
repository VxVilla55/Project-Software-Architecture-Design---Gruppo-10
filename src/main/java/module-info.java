module com.group10 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.group10 to javafx.fxml;
    exports com.group10;

    opens com.group10.controller to javafx.fxml;
    exports com.group10.controller;
}