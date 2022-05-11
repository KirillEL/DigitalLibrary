package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    public static Client client;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("../fxmlFiles/login.fxml"));
        Stage stage1 = new Stage();
        Scene scene1 = new Scene(fxmlLoader1.load());
        stage1.setTitle("RegLog");
        stage1.setScene(scene1);
        stage1.setResizable(false);
        stage1.show();


    }

    public static void main(String[] args) {
        launch();
    }
}