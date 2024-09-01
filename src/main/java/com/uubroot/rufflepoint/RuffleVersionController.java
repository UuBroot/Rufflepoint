package com.uubroot.rufflepoint;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class RuffleVersionController {
    @FXML
    private DialogPane dialog;

    public void initialize(){
        dialog.getButtonTypes().add(ButtonType.CLOSE);
    }
}
