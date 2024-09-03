package com.uubroot.rufflepoint;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;

public class RuffleVersionController {
    @FXML
    private DialogPane dialog;

    @FXML
    private VBox availableVersions;

    @FXML
    private VBox downloadedVersions;

    @FXML
    private ImageView spinner;

    private List<String> downloadedVersionsList = new ArrayList<>();

    public void initialize() throws Exception {
        //adds button to close popup
        dialog.getButtonTypes().add(ButtonType.CLOSE);

        //Loads the loading spinner
        spinner.setImage(new Image("https://media.tenor.com/wpSo-8CrXqUAAAAi/loading-loading-forever.gif"));//TODO:Replace with non online gif
        spinner.setVisible(false);

        //updates already downloaded versions
        updateDownloadedEntries();

        //Downloads json to show all downloadable ruffle versions
        List<RuffleVersion> versions = getVersions();

        //generates versions in gui
        for (int i = 0; i < versions.size(); i++) {
            HBox newItem = new HBox();
            Text versionNumber = new Text();
            Button installButton = new Button();

            versionNumber.setText(versions.get(i).getName());
            installButton.setText("Install");
            int thisI = i;//is done because intelij said so
            installButton.setOnAction(actionEvent -> {
                try {
                    downloadVersion(versions.get(thisI).getFiles());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            newItem.getChildren().add(versionNumber);
            newItem.getChildren().add(installButton);

            availableVersions.getChildren().add(newItem);
        }

    }

    private void updateDownloadedEntries(){
        File ruffleFolder = new File(FolderManager.folderPath+"/ruffle");
        List<File> folders = new ArrayList<>();

        for (File file : Objects.requireNonNull(ruffleFolder.listFiles())) {
            if (file.isDirectory()) {
                folders.add(file);
            }
        }

        downloadedVersionsList.clear();
        downloadedVersions.getChildren().clear();
        for (File folder : folders) {
            downloadedVersionsList.add(folder.getName());

            HBox newItem = new HBox();
            Text versionNumber = new Text();
            Button removeButton = new Button();

            versionNumber.setText(folder.getName());
            removeButton.setText("X");

            removeButton.setOnAction(actionEvent -> {
                try{
                    removeVersion(folder);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            newItem.getChildren().add(removeButton);
            newItem.getChildren().add(versionNumber);

            downloadedVersions.getChildren().add(newItem);
        }


    }

    private void removeVersion(File version) {
        if(version.isDirectory()){
            for(File file: Objects.requireNonNull(version.listFiles())){
                if(file.isDirectory()){
                    removeVersion(file);
                }else{
                    file.delete();
                }
            }

        }
        version.delete();
        updateDownloadedEntries();
    }

    private void downloadVersion(List files) throws IOException {
        URL downloadLink = null;
        String fileName = "";
        spinner.setVisible(true);

        switch (System.getProperty("os.name")){
            case "Linux":
                try{
                    Gson gson = new Gson();
                    downloadLink = new URL(gson.fromJson(files.get(1).toString(), JsonObject.class).get("browser_download_url").getAsString());
                    System.out.println(downloadLink);

                    fileName = gson.fromJson(files.get(1).toString(), JsonObject.class).get("name").getAsString();
                } catch (Exception e) {
                    spinner.setVisible(false);
                    throw new RuntimeException(e);
                }

                break;
            default:
                System.err.println(System.getProperty("os.name")+" not supported");
                System.exit(1);

        }

        //Checks if version already exists
        System.out.println(fileName);
        if(downloadedVersionsList.contains(fileName)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ruffle version already downloaded");
            alert.setHeaderText("Ruffle version already downloaded");
            alert.setContentText("Please download a different one, or delete the current");
            alert.showAndWait();

            return;
        }

        spinner.setVisible(true);
        String pathToFile = FolderManager.folderPath+"/"+fileName;
        System.out.println(pathToFile);

        HttpURLConnection connection = (HttpURLConnection) downloadLink.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(pathToFile)) {

            byte[] buffer = new byte[1024];
            int length;


            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);

            }

        }

        File downloadedFile = new File(pathToFile);
        File outputDir = new File(FolderManager.folderPath+"/ruffle/"+fileName);
        outputDir.mkdir();

        try (InputStream fi = new FileInputStream(downloadedFile);
             GzipCompressorInputStream gzi = new GzipCompressorInputStream(fi);
             TarArchiveInputStream tai = new TarArchiveInputStream(gzi)) {

            TarArchiveEntry entry;
            while ((entry = tai.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    File dir = new File(outputDir, entry.getName());
                    dir.mkdir();

                } else {
                    File file = new File(outputDir, entry.getName());
                    try (OutputStream fo = new FileOutputStream(file)) {
                        IOUtils.copy(tai, fo);

                    }
                }
            }
        }

        try{
            downloadedFile.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateDownloadedEntries();
        spinner.setVisible(false);

    }


    private List<RuffleVersion> getVersions() throws Exception {
        URL url = new URL("https://api.github.com/repos/ruffle-rs/ruffle/releases");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder jsonStringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonStringBuilder.append(line);
        }
        reader.close();

        String jsonString = jsonStringBuilder.toString();

        // Parse the JSON string
        return parseJson(jsonString);
    }

    private List<RuffleVersion> parseJson(String jsonString) throws Exception {
        List<RuffleVersion> list = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        if (jsonElement.isJsonArray()) {
            System.out.println(jsonElement);

            JsonArray jsonArray = jsonElement.getAsJsonArray();
            System.out.println(jsonArray);

            for (int i = 0;i < jsonArray.size();i++){
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                System.out.println(jsonObject.get("tag_name"));

                RuffleVersion newVersion = new RuffleVersion(jsonObject.get("tag_name").getAsString(), jsonObject.get("assets").getAsJsonArray().asList(), jsonObject.get("published_at").getAsString());

                list.add(newVersion);
                System.out.println("--------------------------");

            }

        }else {
            System.err.println("not array");
        }

        return list;
    }
}