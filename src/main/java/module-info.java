module com.uubroot.rufflepoint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;


    opens com.uubroot.rufflepoint to javafx.fxml;
    exports com.uubroot.rufflepoint;
}