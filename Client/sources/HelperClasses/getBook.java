package HelperClasses;

import Controllers.AppController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Hyperlink;

import java.sql.*;

public class getBook {
    public ObservableValue<Integer> id;
    public SimpleStringProperty name;
    public SimpleStringProperty author_name;
    public ObservableValue<Integer> release_date;
    public SimpleStringProperty resource;
    public ObservableValue<Hyperlink> resource_link;
    private Connection connection;
    private Statement st;
    private final String sql_request = "SELECT * FROM library;";
    private ResultSet resultSet;

    public getBook(String nameBook, String authorName, int releaseDate, Hyperlink resource) {
        this.name = new SimpleStringProperty(nameBook);
        this.author_name = new SimpleStringProperty(authorName);
        this.release_date = new SimpleObjectProperty<>(releaseDate);
        this.resource_link = new SimpleObjectProperty<>(resource);
    }

    public void init() {
        try {
            connection = DriverManager.getConnection(AppController.DB_URL, AppController.DB_USERNAME, AppController.DB_PASSWORD);
            st = connection.createStatement();
            resultSet = st.executeQuery(sql_request);

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public getBook() {

    }

    public getBook(int id, String name, String author_name, int release_date, Hyperlink resource) {
        this.id = new SimpleObjectProperty<>(id);
        this.name = new SimpleStringProperty(name);
        this.author_name = new SimpleStringProperty(author_name);
        this.release_date = new SimpleObjectProperty<>(release_date);
        this.resource_link = new SimpleObjectProperty<>(resource);

    }


    public ResultSet getSet() {
        return this.resultSet;
    }
}
