package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminRemoveBookController implements Initializable {

    @FXML
    private Button btn;

    @FXML
    private Text text;

    @FXML
    private TextField textField;


    public String textFromField;


    @FXML
    public AdminController adminController;

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        textFromField = textField.getText();
        try {
            Connection connection = DriverManager.getConnection(AppController.DB_URL, AppController.DB_USERNAME, AppController.DB_PASSWORD);
            PreparedStatement st = connection.prepareStatement("DELETE FROM library WHERE id = ?");
            st.setInt(1, Integer.parseInt(textFromField));






            Statement f = connection.createStatement();
            ResultSet set = f.executeQuery("SELECT id FROM library;");
            boolean isFind = false;
            while(set.next()) {
                if(set.getInt(1) == Integer.parseInt(textFromField)) {
                    st.executeUpdate();
                    isFind = true;
                }
            }
            if(!isFind) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Проверьте id");
                alert.show();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/admin_library.fxml"));
            Parent root = loader.load();
            AdminController adminController = loader.getController();
            adminController.updateTable();




        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
    }


    public void styles() {
        btn.setStyle("-fx-border-color: #DC143C");
        text.setStyle("-fx-font-family: 'JetBrains Mono NL'; -fx-font-size: 14px");


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("i am herte");
        styles();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/admin_library.fxml"));
        try {
            Parent parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
