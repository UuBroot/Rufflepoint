module com.uubroot.rufflepoint {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uubroot.rufflepoint to javafx.fxml;
    exports com.uubroot.rufflepoint;
}