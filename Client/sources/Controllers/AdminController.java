package Controllers;

import HelperClasses.getBook;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public int count = 0;
    public static final String DB_USERNAME = "postgres";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/librarybooks";
    public static final String DB_PASSWORD = "Kirik228123";
    private final  String SQL_SELECT_ALL = "SELECT * FROM library;";
    private getBook gBook;
    private ResultSet set;
    private ObservableList<getBook> list;
    private List<getBook> lst;
    public TableView<getBook> table = createTable();
    private final int dataSize = getDBSize();
    public final int rowsPerPage = 15;
    private List<getBook> data = createData();

    public TableView<getBook> getBookTableView;
    private TableColumn<getBook, Integer> idColumn;
    private TableColumn<getBook, String> nameColumn;
    private TableColumn<getBook, String> authorNameColumn;
    private TableColumn<getBook, Integer> releaseDateColumn;
    private TableColumn<getBook, Hyperlink> resourceColumn;

    public Stage sc;
    public static int countDownloads;

    @FXML
    public Pagination Page_ination;

    @FXML
    public Button btn_leave;

    @FXML
    public Button addBookBtn;

    @FXML
    private Button changeBookBtn;

    @FXML
    private Button removeBookBtn;

    @FXML
    private AnchorPane inner_anchor_pane;

    @FXML
    private Text text_panel_admin;

    @FXML
    private Button updateBtn;

    @FXML
    private ImageView imgView;

    @FXML
    private Button btnCountDownloads;

    @FXML
    void onBtnCountDownloadClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Кол-во скачиваний:\t" + countDownloads);
        alert.show();

    }



    @FXML
    void onUpdateBtnClicked(ActionEvent event) {
        updateTable();
    }


    @FXML
    void addBookBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/adminAddBook.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        //Stage st = (Stage) btn_leave.getScene().getWindow();
        //st.close();
        updateTable();


    }

    @FXML
    void changeBookBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/adminEditBook.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(loader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void removeBookBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFiles/adminRemoveBook.fxml"));
        Scene parent = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(parent);
        stage.setResizable(false);
        stage.show();



    }


    @FXML
    public void onBtnLeaveClicked(ActionEvent event) throws IOException {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sc = new Stage();
        pageCount();
        styles();
        imgView.setImage(new Image("D:\\Desktop\\NSTU\\CourseProjects\\javaCourseWork\\Client\\images\\logo2.jpg"));
    }



    public TableView<getBook> createTable() {
        getBookTableView = new TableView<>();
        idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(param -> param.getValue().id);
        idColumn.setPrefWidth(75);
        nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(param -> param.getValue().name);
        nameColumn.setPrefWidth(220);
        authorNameColumn = new TableColumn<>("Автор");
        authorNameColumn.setCellValueFactory(param -> param.getValue().author_name);
        authorNameColumn.setPrefWidth(200);
        releaseDateColumn = new TableColumn<>("Дата выпуска");
        releaseDateColumn.setCellValueFactory(param -> param.getValue().release_date);
        releaseDateColumn.setPrefWidth(200);

        TableColumn<getBook, Hyperlink> resourceColumn = new TableColumn<>("Ссылка");
        resourceColumn.setCellValueFactory(param -> param.getValue().resource_link);
        resourceColumn.setPrefWidth(200);
        getBookTableView.getColumns().addAll(idColumn, nameColumn, authorNameColumn, releaseDateColumn, resourceColumn);
        return getBookTableView;
    }

    public void updateTable() {
        data = createData();
        table = createTable();
        pageCount();

    }

    public void pageCount() {
        int pages = 1;
        if(data.size() % rowsPerPage == 0) {
            pages = data.size() / rowsPerPage;
        }
        else if(data.size() > rowsPerPage) {
            pages = data.size() / rowsPerPage + 1;

        }
        Page_ination.setPageCount(pages);
        Page_ination.setPageFactory(this::createPage);
    }



    public Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return table;
    }

    public List<getBook> createData() {
        List<getBook> listData = new ArrayList<>(getDBSize());

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


    public void styles() {
        changeBookBtn.setStyle("-fx-background-color: lightgreen; -fx-font-family: 'JetBrains Mono NL'");
        removeBookBtn.setStyle("-fx-background-color: lightgreen");
        addBookBtn.setStyle("-fx-background-color: lightgreen");
        updateBtn.setStyle("-fx-background-color: lightgreen");

        btn_leave.setStyle("-fx-background-color: #A52A2A");





    }





}
