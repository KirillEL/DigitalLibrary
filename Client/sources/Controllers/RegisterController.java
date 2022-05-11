package Controllers;

import Main.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private String textFromField1;
    private String textFromField2;
    private String textFromField3;
    private final String DB_USERNAME = "postgres";
    private final String DB_URL = "jdbc:postgresql://localhost:5432/users";
    private final String DB_PASSWORD = "Kirik228123";
    private int i = 1;

    @FXML
    private Button btn_reg;

    @FXML
    private TextField pass_reg_text_field;

    @FXML
    private TextField pass_reg_text_field_again;

    @FXML
    private TextField reg_log_text_field;


    @FXML
    void BtnRegClicked(ActionEvent event) {
        /*Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("warning");
        alert.setHeaderText("Проверьте данные");


        textFromField1 = reg_log_text_field.getText();
        textFromField2 = pass_reg_text_field.getText();
        textFromField3 = pass_reg_text_field_again.getText();

        if(Objects.equals(textFromField1, "") || Objects.equals(textFromField2, "") || Objects.equals(textFromField3, "")) {
            alert.show();
            reg_log_text_field.setStyle("-fx-border-color: red");
            pass_reg_text_field.setStyle("-fx-border-color: red");
            pass_reg_text_field_again.setStyle("-fx-border-color: red");
        }
        else if(Objects.equals(textFromField2, textFromField3)) {
            DBUsers();
            try {
                Stage out = (Stage) btn_reg.getScene().getWindow();
                out.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/login.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        else {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle("alert");
            alert1.setContentText("Проверьте введенные поля!, Пароли должны совпадать!");
            alert1.show();
        }*/

    }


    void DBUsers(){
        try {
            String SQL_INSERT_REQUEST = "INSERT INTO users(id, login, password, role) VALUES(?,?,?, ?)";
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_REQUEST);
            Statement st = connection.createStatement();
            ResultSet sq = st.executeQuery("SELECT * FROM users;");
            while(sq.next()){
                i++;
            }

            preparedStatement.setInt(1, i);

            preparedStatement.setString(2, textFromField1);
            preparedStatement.setString(3, textFromField2);
            if(i == 1) preparedStatement.setInt(4, 1);
            else
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_reg.setOnAction(e -> {
            String login_text = reg_log_text_field.getText();
            String password1 = pass_reg_text_field.getText();
            String password2 = pass_reg_text_field_again.getText();
            try {
                String messageFromServer = HelloApplication.client.Register(login_text,password1,password2);
                if(messageFromServer.equalsIgnoreCase("register success")) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/login.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    Stage st = (Stage) btn_reg.getScene().getWindow();
                    st.hide();
                    stage.showAndWait();


                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }


        });
    }
}
