module com.group10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    
    opens com.group10.controller to javafx.fxml;
    opens com.group10.controller.track to javafx.fxml;
    opens com.group10.controller.playlist to javafx.fxml;
    
    exports com.group10;
    exports com.group10.controller;
}