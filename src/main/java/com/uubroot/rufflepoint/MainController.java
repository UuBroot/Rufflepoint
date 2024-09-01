package com.uubroot.rufflepoint;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    public VBox games;

    private RuffleVersionApplication ruffleVersionApplication = new RuffleVersionApplication();

    public void openRuffleVersions() throws IOException {
        System.out.println("Open ruffle versions");
        ruffleVersionApplication.openRuffleVersion();

    }

    private void downloadRuffleVersion(String version){

    }

}