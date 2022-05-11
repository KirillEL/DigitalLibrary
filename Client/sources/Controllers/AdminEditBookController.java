package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminEditBookController implements Initializable {

    @FXML
    private TextField authorNameField;

    @FXML
    private Button changeBtn;

    @FXML
    private TextField dataReleaseField;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField resourceLinkField;

    public int id;
    public String name;
    public String author_name;
    public int releaseDate;
    public String resource;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        changeBtn.setOnAction(e -> {
            try {
                if(Objects.equals(idTextField.getText(), "")) {
                    id = 0;

                }
                id = Integer.parseInt(idTextField.getText());
                name = nameTextField.getText();
                author_name = authorNameField.getText();
                releaseDate = Integer.parseInt(dataReleaseField.getText());
                resource = resourceLinkField.getText();
                Connection connection = DriverManager.getConnection(AdminController.DB_URL, AdminController.DB_USERNAME, AdminController.DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement("update library set name =?, author=?, releasedate=?, resource=? where (id = ?);");

                preparedStatement.setString(1,name);
                preparedStatement.setString(2, author_name);
                preparedStatement.setInt(3, releaseDate);
                preparedStatement.setString(4, resource);
                preparedStatement.setInt(5, id);
                preparedStatement.executeUpdate();

            }
            catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });

    }

    public void setData() {
        if(Objects.equals(idTextField.getText(), "")) {
            id = 0;
        }
        id = Integer.parseInt(idTextField.getText());
        name = nameTextField.getText();
        author_name = authorNameField.getText();
        releaseDate = Integer.parseInt(dataReleaseField.getText());
        resource = resourceLinkField.getText();
    }
}
