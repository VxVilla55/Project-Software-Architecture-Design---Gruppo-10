module com.group10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.graphics;
    requires com.google.gson;

    opens com.group10.controller;
    opens com.group10.controller.track;
    opens com.group10.controller.playlist;

    opens com.group10.model to com.google.gson;
    opens com.group10.model.persistence to com.google.gson;

    exports com.group10;
    exports com.group10.controller;
}