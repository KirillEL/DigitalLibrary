package Controllers;

import Main.Client;
import Main.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class loginController implements Initializable {


    public FXMLLoader loader, loader_admin;
    private Scene scene;
    private Stage stage;
    private final HelloApplication appl = new HelloApplication();
    public static final String DB_USERNAME = "postgres";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/users";
    public static final String DB_PASSWORD = "Kirik228123";



    @FXML
    private TextField log_text_field;

    @FXML
    private PasswordField pass_text_field;

    @FXML
    public Button btn_join;

    @FXML
    private Button btn_reg;

    @FXML
    private Text text_error;

    @FXML
    private BorderPane header_border_pane;

    @FXML
    private Text header_text;

    @FXML
    void onBtnJoinClick(ActionEvent event) throws IOException {

       /* try {
            String log_text = log_text_field.getText();
            String pass_text = pass_text_field.getText();
            String str = HelloApplication.client.Login(log_text, pass_text);
            if(str.equalsIgnoreCase("login success")) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/library.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                //stage.show();
                stage.showAndWait();

                Stage st = (Stage) btn_join.getScene().getWindow();
                st.hide();
            }


        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }*/


        /*try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String SQL_SELECT_ALL = "SELECT * FROM users;";
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(SQL_SELECT_ALL);
            String log_text = log_text_field.getText();
            String pass_text = pass_text_field.getText();
            System.out.println(log_text);
            System.out.println(pass_text);
            while(set.next()) {
                if(Objects.equals(log_text, set.getString("login")) && Objects.equals(pass_text, set.getString("password"))) {
                    if(set.getInt(4) == 0) {
                        loader = new FXMLLoader(getClass().getResource("../fxmlFiles/library.fxml"));
                        scene = new Scene(loader.load());
                        stage = new Stage();
                        stage.setScene(scene);
                        stage.show();


                        Stage st = (Stage) btn_join.getScene().getWindow();
                        st.close();

                    }
                    else {
                        loader_admin = new FXMLLoader(getClass().getResource("../fxmlFiles/admin_library.fxml"));
                        Scene scene_admin = new Scene(loader_admin.load());
                        Stage stage_admin = new Stage();
                        stage_admin.setScene(scene_admin);
                        stage_admin.show();

                        Stage s = (Stage) btn_join.getScene().getWindow();
                        s.close();
                    }
                }
                else {
                    log_text_field.setStyle("-fx-border-color: red");
                    pass_text_field.setStyle("-fx-border-color: red");



                }
            }

        }
        catch (SQLException exc){
            System.out.println(exc.getMessage());
        }*/
    }


    @FXML
    void onBtnRegClick(ActionEvent event) throws IOException {
        /*loader = new FXMLLoader(getClass().getResource("../fxmlFiles/register.fxml"));
        scene = new Scene(loader.load());
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage st = (Stage) btn_reg.getScene().getWindow();
        st.close();*/
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelloApplication.client = new Client();
        HelloApplication.client.enable();
        HelloApplication.client.start();

        btn_join.setOnAction(e -> {
            String log_text = log_text_field.getText();
            String pass_text = pass_text_field.getText();
            String str = null;
            try {
                str = HelloApplication.client.Login(log_text, pass_text);
                System.out.println(str);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
            if(str.equalsIgnoreCase("login success")) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/library.fxml"));
                Stage stage = new Stage();
                Scene scene = null;
                try {
                    scene = new Scene(loader.load());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                stage.setScene(scene);
                //stage.show();
                stage.showAndWait();

                Stage st = (Stage) btn_join.getScene().getWindow();
                st.hide();
            }
        });

        btn_reg.setOnAction(e -> {
            loader = new FXMLLoader(getClass().getResource("../fxmlFiles/register.fxml"));
            try {
                scene = new Scene(loader.load());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Stage st = (Stage) btn_reg.getScene().getWindow();
        st.close();
        });

        text_error.setVisible(false);
        styles();
    }


    public void styles() {

    }
}
