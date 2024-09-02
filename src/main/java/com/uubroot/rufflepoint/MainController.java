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
        switch (System.getProperty("os.name")){
            case "Linux":
                String url = "https://github.com/ruffle-rs/ruffle/releases/download/"+version+"/"+version+"-linux-x86_64.tar.gz";
                break;
            default:
                System.err.println(System.getProperty("os.name")+" not supported");
                System.exit(1);

        }

    }
}