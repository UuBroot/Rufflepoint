package com.uubroot.rufflepoint;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuffleVersionController {
    @FXML
    private DialogPane dialog;

    @FXML
    private VBox availableVersions;

    public void initialize() throws Exception {
        dialog.getButtonTypes().add(ButtonType.CLOSE);

        List<String> versions = getVersions();

        versions.add("1.2");
        for (int i = 0; i < versions.size(); i++) {
            HBox newItem = new HBox();
            Text versionNumber = new Text();
            Button installButton = new Button();

            versionNumber.setText(versions.get(i));
            installButton.setText("Install");

            newItem.getChildren().add(versionNumber);
            newItem.getChildren().add(installButton);

            availableVersions.getChildren().add(newItem);
        }

    }

    private List<String> getVersions() throws Exception {
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
        List<String> ids = parseJson(jsonString);
        return ids;
    }

    private List<String> parseJson(String jsonString) throws Exception {
        List<String> list = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        if (jsonElement.isJsonArray()) {
            System.out.println(jsonElement);

            JsonArray jsonArray = jsonElement.getAsJsonArray();
            System.out.println(jsonArray);

            for (int i = 0;i < jsonArray.size();i++){
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                System.out.println(jsonObject.get("tag_name"));
                list.add(jsonObject.get("tag_name").getAsString());
                System.out.println("--------------------------");

            }

        }else {
            System.err.println("not array");
        }

        return list;
    }
}