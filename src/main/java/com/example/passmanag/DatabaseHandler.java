package com.example.passmanag;

import org.bouncycastle.crypto.agreement.srp.SRP6Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.SortedMap;

public class DatabaseHandler extends Configs {
    Connection dbConnection;
    public static ResultSet rSet;
    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString =  "jdbc:mysql://localhost/psmg?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false";

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }
    public void RegistrationUser (User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" + Const.USER_LOGIN + "," + Const.USER_PASSWORD + ")" + "VALUES(?,?)";

        try (PreparedStatement prSt = getDbConnection().prepareStatement(insert)){
            prSt.setString(1,user.getLogin());
            prSt.setString(2, user.getPassword());

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void AddNewLogPass (String application, String cipusername, String cippassword, String ciphermet, String unique, String kluch) {
        String insert = "INSERT INTO " + Const.USER_TABLE1 + "(" + Const.USER_APPLICATION + "," + Const.USER_LOGIN1 + "," + Const.USER_PASSWORD1 + "," + Const.USER_TYPECIPHER
                + "," + Const.USER_UNIKAL +"," + Const.USER_KLUCH + ")" + "VALUES(?,?,?,?,?,?)";

        try (PreparedStatement prSt = getDbConnection().prepareStatement(insert)) {
            prSt.setString(1,application);
            prSt.setString(2,cipusername);
            prSt.setString(3,cippassword);
            prSt.setString(4,ciphermet);
            prSt.setString(5,unique);
            prSt.setString(6,kluch);

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + "=? AND " + Const.USER_PASSWORD + "=?";

        try {
            getData();
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1,user.getLogin());
            prSt.setString(2, user.getPassword());
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return resSet;
    }

    public ResultSet getUnique(UnikalLogin unikalLogin){
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE1 + " WHERE " + Const.USER_UNIKAL + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1,unikalLogin.getUnikal());
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return resSet;
    }
   /* public ResultSet getInfo(ApplicationData applicationData){
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE1 + " WHERE " + Const.USER_APPLICATION + "=? AND " + Const.USER_LOGIN1 + "=? AND " + Const.USER_PASSWORD1 + "=? AND "
                + Const.USER_TYPECIPHER
                + "=? AND "+ Const.USER_UNIKAL + "=? AND " + Const.USER_KLUCH + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1,applicationData.getApplication());
            prSt.setString(2,applicationData.getUsername());
            prSt.setString(3, applicationData.getPassword());
            prSt.setString(4, applicationData.getTypecipher());
            prSt.setString(5, applicationData.getUnikal());
            prSt.setString(6, applicationData.getKluch());
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return resSet;
    }*/
    public ResultSet getData() {
        ResultSet resultSet = null;
        String select = "SELECT application, username, password, unikal, typecipher, kluch FROM new_table WHERE unikal = ?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, HelloController.getLastLoginText());
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        rSet = resultSet;
        return resultSet;
    }
}
