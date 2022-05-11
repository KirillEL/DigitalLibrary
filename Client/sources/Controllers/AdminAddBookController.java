package Controllers;

import HelperClasses.getBook;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminAddBookController implements Initializable {

    public  int count = 0;
    public AdminController ctrl;
    public Stage stage;
    public FXMLLoader loaderBTW = new FXMLLoader(getClass().getResource("../fxmlFiles/admin_library.fxml"));
    public Parent root;
    public List<getBook> dataList;

    @FXML
    private Button addBookButton;

    @FXML
    private TextField dataReleaseField;

    @FXML
    private FlowPane headerPane;

    @FXML
    private TextField nameAuthorBookField;

    @FXML
    private TextField nameBookField;

    @FXML
    private TextField resourceField;

    @FXML
    private Text text_error;


    @FXML
    void onAddBookClicked(ActionEvent event) throws IOException {
        getData();
        boolean isCorrectData = isCorrectDataInput();
        root = loaderBTW.load();
        ctrl = loaderBTW.getController();



        try {
            if(isCorrectData) {
                Connection connection = DriverManager.getConnection(AdminController.DB_URL, AdminController.DB_USERNAME, AdminController.DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO library(id,name,author,releasedate,resource) VALUES(?,?,?,?,?)");
                preparedStatement.setInt(1,getDBSize()+1);
                preparedStatement.setString(2, nameBook);
                preparedStatement.setString(3, authorName);
                preparedStatement.setInt(4, releaseDate);
                preparedStatement.setString(5, resource);
                preparedStatement.executeUpdate();
                Hyperlink link = new Hyperlink();
                link.setText(resource);
                Stage st = (Stage) addBookButton.getScene().getWindow();

                link.setOnAction(e -> {
                    Application a = new Application() {
                        @Override
                        public void start(Stage stage) throws Exception {

                        }
                    };
                    a.getHostServices().showDocument(link.getText());
                });
                st.close();

                //FXMLLoader loader2 = new FXMLLoader(getClass().getResource("../fxmlFiles/admin_library.fxml"));
                //Scene scene = new Scene(loader2.load());
                //Stage stage = new Stage();
                //stage.setScene(scene);
                //stage.show();


            }
            else {
                text_error.setVisible(true);


            }

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public String nameBook, authorName, resource;
    public int releaseDate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text_error.setVisible(false);
        styles();

    }

    public int getDBSize() {
        try {
            Connection con = DriverManager.getConnection(AdminController.DB_URL, AdminController.DB_USERNAME, AdminController.DB_PASSWORD);
            Statement st = con.createStatement();
            ResultSet set = st.executeQuery("SELECT id FROM library;");
            while(set.next()) {
                count++;
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return count;
    }





    public void getData() {
        nameBook = nameBookField.getText();
        authorName = nameAuthorBookField.getText();
        if(dataReleaseField.getText() == "") {
            releaseDate = 0;
        }
        else
        releaseDate = Integer.parseInt(String.valueOf(dataReleaseField.getText()));
        resource = resourceField.getText();


    }

    public void styles() {
        nameBookField.setFont(new Font("JetBrains Mono", 12));
        nameAuthorBookField.setFont(new Font("JetBrains Mono",12));
        dataReleaseField.setFont(new Font("JetBrains Mono",12));
        resourceField.setFont(new Font("JetBrains Mono",12));

        addBookButton.setStyle("-fx-background-color: FF4500");
        addBookButton.setFont(new Font("Consolas", 14));

    }

    public boolean isCorrectDataInput() {
        boolean f = true;
        if(Objects.equals(nameBook, "")) {
            nameBookField.setStyle("-fx-border-color: red");
            f = false;
        }
        if(Objects.equals(authorName, "")) {
            nameAuthorBookField.setStyle("-fx-border-color: red");
            f = false;
        }
        if(releaseDate == 0) {
            dataReleaseField.setStyle("-fx-border-color: red");
            f = false;
        }
        if(Objects.equals(resource, "")) {
            resourceField.setStyle("-fx-border-color: red");
            f = false;
        }
        return f;
    }




}
