package com.example.passmanag;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addNewApplication;

    @FXML
    private AnchorPane inScrollPane;

    @FXML
    private Button removeApplication;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vBox;

    @FXML
    void initialize() {
        boolean A = HelloController.isA();
        boolean B = NewDataWindow.isB();
        if (A == true) {
            UpdateData();
        }
        if (B == true) {
            UpdateData();
        }

        addNewApplication.setOnAction(actionEvent -> {
            addNewApplication.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("newdatawindow.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });
    }

    public void UpdateData() {
        ResultSet resultSet = DatabaseHandler.rSet;
            while (true) {
                try {

                    if (!resultSet.next()) break;

                    String application = resultSet.getString("application");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String unikl = resultSet.getString("unikal");
                    String typecipher = resultSet.getString("typecipher");
                    String kluch = resultSet.getString("kluch");
                    if (typecipher.equals("AES-256")) {
                        /*username = Cipher.decryptAES(username, kluch);
                        password = Cipher.decryptAES(password, kluch);*/
                        Label dataLabel = new Label("Application: " + application + "\nUsername: " + username + "\nPassword: " + password);
                        vBox.getChildren().add(dataLabel);
                    }
                    if (typecipher.equals("Twofish")) {
                        username = Cipher.decryptTwoFish(username, kluch.getBytes());
                        password = Cipher.decryptTwoFish(password, kluch.getBytes());
                        Label dataLabel = new Label("Application: " + application + "\nUsername: " + username + "\nPassword: " + password);
                        vBox.getChildren().add(dataLabel);
                    } else {
                        Label dataLabel = new Label("Application: " + application + " Username: " + username + " Password: " + password);
                        vBox.getChildren().add(dataLabel);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}

