module com.uubroot.rufflepoint {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.commons.compress;
    requires org.apache.commons.io;


    opens com.uubroot.rufflepoint to javafx.fxml;
    exports com.uubroot.rufflepoint;
}