package com.example.passmanag;

import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import javax.crypto.SecretKey;

public class NewDataWindow {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Label labelTest;

    @FXML
    private TextField nameApplication;

    @FXML
    private Button addnewlogpass;

    @FXML
    private ComboBox<String> choosecipher;

    @FXML
    private CheckBox cipherornot;

    @FXML
    private Button goback2;

    @FXML
    private TextField newlogfield;

    @FXML
    private PasswordField newpassfield;


    public TextField getNewlogfield() {
        return newlogfield;
    }

    public void setNewlogfield(TextField newlogfield) {
        this.newlogfield = newlogfield;
    }

    public PasswordField getNewpassfield() {
        return newpassfield;
    }

    public void setNewpassfield(PasswordField newpassfield) {
        this.newpassfield = newpassfield;
    }

    @FXML
    void initialize() {

        ObservableList<String> langs = FXCollections.observableArrayList("AES-256", "Twofish");
        choosecipher.setItems(langs);
        choosecipher.setValue("AES-256");


        addnewlogpass.setOnAction(actionEvent -> {
            AddNewApplication();

            addnewlogpass.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainwindow.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });
        goback2.setOnAction(actionEvent -> {
            goback2.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainwindow.fxml"));

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
    private static boolean B;
    public static boolean isB() {
        return B;
    }
    private void AddNewApplication() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String unique = HelloController.getLastLoginText();
        String application = nameApplication.getText();
        String cipusername = "";
        String cippassword = "";
        String kluch = "";
        String ciphermet = choosecipher.getValue();

        if (choosecipher.getValue().equals("AES-256") && cipherornot.isSelected()) {
            try {
                SecretKey key = Cipher.generateAESKey();
                cipusername = Cipher.encryptAES(newlogfield.getText(), key);
                cippassword = Cipher.encryptAES(newpassfield.getText(), key);
                kluch = String.valueOf(key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (choosecipher.getValue().equals("Twofish") && cipherornot.isSelected()) {
            byte[] key = Cipher.generateTwoFishKey();
            cipusername = Cipher.encryptTwoFish(newlogfield.getText(), key);
            cippassword = Cipher.encryptTwoFish(newpassfield.getText(), key);
            System.out.println(ciphermet);
            System.out.println(unique);
            kluch = String.valueOf(key);
        } else {
            cipusername = newlogfield.getText();
            cippassword = newpassfield.getText();
            ciphermet = "none";
            kluch = "none";
        }
        dbHandler.AddNewLogPass(application, cipusername, cippassword, ciphermet, unique, kluch);
        B = true;
    }
}