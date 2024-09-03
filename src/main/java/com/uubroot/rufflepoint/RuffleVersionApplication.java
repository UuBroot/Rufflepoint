package com.uubroot.rufflepoint;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class RuffleVersionApplication {

    Dialog<Object> ruffleVersionsDialog = new Dialog<>();

    public void openRuffleVersion() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("version-popup.fxml"));

        DialogPane dialogContent = fxmlLoader.load();

        ruffleVersionsDialog.setDialogPane(dialogContent);
        ruffleVersionsDialog.show();
    }
    public void close(){
        ruffleVersionsDialog.close();
    }
}
