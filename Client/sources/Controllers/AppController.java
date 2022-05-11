package Controllers;

import HelperClasses.getBook;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AppController implements Initializable {

    public int count = 0;
    public static final String DB_USERNAME = "postgres";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/librarybooks";
    public static final String DB_PASSWORD = "Kirik228123";
    private final  String SQL_SELECT_ALL = "SELECT * FROM library;";
    private getBook gBook;
    private ResultSet set;
    private ObservableList<getBook> list;
    private List<getBook> lst;
    private final TableView<getBook> table = createTable();
    private final int dataSize = getDBSize();
    private final int rowsPerPage = 5;
    private final List<getBook> data = createData();

    private TableView<getBook> getBookTableView;
    private TableColumn<getBook, Integer> idColumn;
    private TableColumn<getBook, String> nameColumn;
    private TableColumn<getBook, String> authorNameColumn;
    private TableColumn<getBook, Integer> releaseDateColumn;
    private TableColumn<getBook, Hyperlink> resourceColumn;

    private FXMLLoader loader_close;

    @FXML
    private FlowPane header_pane;

    @FXML
    private Button btn_leave;

    @FXML
    private Pagination Page_ination;

    @FXML
    private Button findBtn;

    @FXML
    private ImageView logo_img;

    @FXML
    public TextField findTextField = new TextField();

    @FXML
    public TextArea textArea = new TextArea();

    @FXML
    void OnBtnLeavePressed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("alert");
        alert.setHeaderText("ПОДТВЕРЖДЕНИЕ");
        alert.setContentText("Вы уверены что хотите покинуть библиотеку?");
        if(alert.showAndWait().get() == ButtonType.OK) {
            Stage sc = (Stage) btn_leave.getScene().getWindow();
            sc.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }

    }



    @FXML
    void onEnterPressTextField(ActionEvent event) {
        String getFromField = findTextField.getText();
        lst = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement st = connection.createStatement();
            ResultSet set = st.executeQuery(SQL_SELECT_ALL);
            while(set.next()) {

                if(set.getString("name").contains(getFromField) || set.getString("author").contains(getFromField) || String.valueOf(set.getInt(4)).equals(getFromField)) {
                    Hyperlink hyperlink = new Hyperlink(set.getString(5));
                    lst.add(new getBook(set.getInt(1), set.getString(2), set.getString(3), set.getInt(4), hyperlink));
                    table.setItems(FXCollections.observableList(lst));
                    hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Application b = new Application() {
                                @Override
                                public void start(Stage stage) throws Exception {

                                }
                            };
                            b.getHostServices().showDocument(hyperlink.getText());
                            AdminController.countDownloads++;
                        }


                    });
                }
            }
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }

    }

    @FXML
    void onFindBtnClick(ActionEvent event) {
        String getFromField = findTextField.getText();
        lst = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement st = connection.createStatement();
            ResultSet set = st.executeQuery(SQL_SELECT_ALL);
            while(set.next()) {

                if(set.getString("name").contains(getFromField) || set.getString("author").contains(getFromField) || String.valueOf(set.getInt(4)).equals(getFromField)) {
                    Hyperlink hyperlink = new Hyperlink(set.getString(5));
                    lst.add(new getBook(set.getInt(1), set.getString(2), set.getString(3), set.getInt(4), hyperlink));
                        table.setItems(FXCollections.observableList(lst));
                        hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Application b = new Application() {
                                    @Override
                                    public void start(Stage stage) throws Exception {

                                    }
                                };
                                b.getHostServices().showDocument(hyperlink.getText());
                            }


                        });
                }
            }
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       Page_ination.setPageFactory(this::createPage);
       logo_img.setImage(new Image("D:\\Desktop\\NSTU\\CourseProjects\\javaCourseWork\\Client\\images\\logo2.jpg"));
       styleSettings();


    }

    public TableView<getBook> createTable() {
        getBookTableView = new TableView<>();
        idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(param -> param.getValue().id);
        idColumn.setPrefWidth(75);
        nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(param -> param.getValue().name);
        nameColumn.setPrefWidth(300);
        authorNameColumn = new TableColumn<>("Автор");
        authorNameColumn.setCellValueFactory(param -> param.getValue().author_name);
        authorNameColumn.setPrefWidth(300);
        releaseDateColumn = new TableColumn<>("Дата выпуска");
        releaseDateColumn.setCellValueFactory(param -> param.getValue().release_date);
        releaseDateColumn.setPrefWidth(200);

        TableColumn<getBook, Hyperlink> resourceColumn = new TableColumn<>("Ссылка");
        resourceColumn.setCellValueFactory(param -> param.getValue().resource_link);
        resourceColumn.setPrefWidth(350);
        getBookTableView.getColumns().addAll(idColumn, nameColumn, authorNameColumn, releaseDateColumn, resourceColumn);
        return getBookTableView;
    }




    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, dataSize);
        table.setItems(FXCollections.observableArrayList(data));

        return table;
    }

    private List<getBook> createData() {
        List<getBook> listData = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement st = con.createStatement();
            set = st.executeQuery("SELECT * FROM library;");
            while(set.next()) {
                Hyperlink link = new Hyperlink(set.getString(5));
                listData.add(new getBook(set.getInt(1), set.getString(2), set.getString(3), set.getInt(4), link));
                link.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Application a = new Application() {
                            @Override
                            public void start(Stage stage) throws Exception {

                            }

                        };
                        a.getHostServices().showDocument(link.getText());
                    }
                });
            }

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return listData;
    }

    public int getDBSize() {
        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement st = con.createStatement();
            ResultSet set = st.executeQuery("SELECT * FROM library;");
            while(set.next()) {
                count++;
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return count;
    }

    public void styleSettings() {
        Page_ination.setStyle("-fx-font-size: 15px");
        Page_ination.setPageCount(1);
        Page_ination.setPrefHeight(500);
        Page_ination.setStyle("-fx-font-family: 'JetBrains Mono ExtraBold'");
        btn_leave.setStyle("-fx-background-color: lightgreen; -fx-font-family: 'JetBrains Mono NL'; -fx-font-size: 17px");


    }


}
