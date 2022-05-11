import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {

    public static Socket clientSocket;
    public static ServerSocket serverSocket;


    public static String DB_URL = "jdbc:postgresql://localhost:5432/users";
    public static String DB_USERNAME = "postgres";
    public static String DB_PASSWORD = "Kirik228123";

    public static Connection connectionDB;
    static ExecutorService executeIt = Executors.newFixedThreadPool(5);

    public static HashMap<Integer, Server> clients_library = new HashMap<>();


    public static void main(String[] args) throws IOException {

        StartServer startServer = new StartServer();



        try(ServerSocket server = new ServerSocket(8080);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            serverSocket = server;

            try {
                connectionDB = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                System.out.println("success connection to DB");
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
            int i = 0;

            while(!server.isClosed()) {

                if(bufferedReader.ready()) {
                    String serverCommand = bufferedReader.readLine();
                    if(serverCommand.equalsIgnoreCase("leave")) {
                        server.close();
                    }
                }

                clientSocket = server.accept();


                clients_library.put(++i , new Server(clientSocket,i));
                executeIt.execute(clients_library.get(i));


            }
            executeIt.shutdown();

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }


}
