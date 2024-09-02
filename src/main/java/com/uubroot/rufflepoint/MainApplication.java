package com.uubroot.rufflepoint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Rufflepoint");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        setupFolder();
        launch();

    }

    public static void setupFolder(){

        switch (System.getProperty("os.name")){
            case "Linux":
                String homeDir = System.getProperty("user.home");
                String fullPath = homeDir + File.separator+".local"+ File.separator + "share" + File.separator + "Rufflepoint";//TODO:Fix weird full path
                System.out.println("fp:"+fullPath);

                File dir = new File(fullPath + "/Rufflepoint").getParentFile();
                System.out.println(dir);
                if (dir != null && !dir.exists()) {

                    if (dir.mkdirs()) {
                        System.out.println("Directory created: " + dir.getAbsolutePath());
                        //Ruffle Directory
                        File ruffleDir = new File(fullPath+"/ruffle");
                        if(ruffleDir.mkdirs()){
                            System.out.println("Directory created: " + ruffleDir.getAbsolutePath());
                        }else {
                            System.err.println("Failed to create directory");
                        }
                    } else {
                        System.err.println("Failed to create directory");
                    }
                }else {
                    System.err.println("directory exists");
                }

                break;
            default:
                System.err.println(System.getProperty("os.name")+" not supported");
                System.exit(1);

        }
    }
}