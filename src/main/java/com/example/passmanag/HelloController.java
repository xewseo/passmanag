package com.example.passmanag;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logbtn;

    @FXML
    private PasswordField pwfield;

    @FXML
    private Button regbtn;

    @FXML
    private TextField textfield;

    public TextField getTextfield() {
        return textfield;
    }
    private static String lastLoginText;
    public void setTextfield(TextField textfield) {
        this.textfield = textfield;
    }

    @FXML
    void initialize() {
        regbtn.setOnAction((actionEvent -> {
            regbtn.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("regwindow.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }));

        logbtn.setOnAction(actionEvent -> {
            String loginText = textfield.getText().trim();
            String passText = pwfield.getText().trim();


            if (!loginText.equals("") && !passText.equals("")) {
                lastLoginText = loginText;
                loginUser(loginText,passText);
                checkUser(loginText);
            }
            else
                System.out.println("Логин и пароль пустые");
        });
    }
    private static boolean A;
    public static boolean isA() {
        return A;
    }
    public static String getLastLoginText() { return lastLoginText; }

    private void loginUser(String loginText, String passText) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(passText);
        user.setUnique(loginText);
        ResultSet result = dbHandler.getUser(user);

        int counter = 0;

        while(true) {
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }

        if (counter >= 1) {
            System.out.println("Пользователь авторизирован");
            regbtn.getScene().getWindow().hide();

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

        } else {
            Shake userLogAnim = new Shake(textfield);
            Shake userPassAnim = new Shake(pwfield);
            userLogAnim.playAnim();
            userPassAnim.playAnim();
        }
    }
    public void checkUser(String unique) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        UnikalLogin unikalLogin = new UnikalLogin();
        unikalLogin.setUnikal(unique);
        ResultSet result = dbHandler.getUnique(unikalLogin);

        int counter = 0;

        while (true) {
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }
        if (counter >= 1) {
            A = true;
            System.out.println("Данные пользователя найдены");
        }
        else{ System.out.println("Данных о пользователе нет"); }
    }
}
