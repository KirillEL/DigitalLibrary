import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class Server implements Runnable {

    public Socket clientSocket;
    ObjectOutputStream obj_out;
    ObjectInputStream obj_in;
    private static StartServer start_server;
    public boolean isConnected = false;
    int n;
    int i = 1;

    public Server(Socket socket, int num) {
        n = num;
        clientSocket = socket;
        isConnected = true;
        try {

            obj_out = new ObjectOutputStream(clientSocket.getOutputStream());
            obj_in = new ObjectInputStream(clientSocket.getInputStream());
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while(!clientSocket.isClosed()) {
                String input = obj_in.readUTF();

                System.out.println("message from client: " + input);
                if(input.equalsIgnoreCase("register")) {
                    input = obj_in.readUTF();
                    String[] args = input.split(" ");
                    String sql_request = "INSERT INTO users(id, login, password, role) VALUES(?,?,?,?);";
                    PreparedStatement preparedStatement = StartServer.connectionDB.prepareStatement(sql_request);
                    Statement st = StartServer.connectionDB.createStatement();
                    ResultSet sq = st.executeQuery("SELECT * FROM users;");
                    while(sq.next()){
                        i++;
                    }
                    preparedStatement.setInt(1,i+1);
                    preparedStatement.setString(2, args[0]);
                    preparedStatement.setString(3, args[1]);
                    preparedStatement.setInt(4, 0);
                    preparedStatement.executeUpdate();
                    obj_out.writeUTF("register success");
                    obj_out.flush();
                    Thread.sleep(500);
                }
                else if(input.equalsIgnoreCase("login")) {
                    System.out.println("i am in server");
                    input = obj_in.readUTF();
                    String[] args = input.split(" ");
                    String sql_request = "SELECT id,role from users where (login = ?) AND password =?;";
                    PreparedStatement preparedStatement = StartServer.connectionDB.prepareStatement(sql_request);
                    preparedStatement.setString(1, args[0]);
                    preparedStatement.setString(2, args[1]);
                    ResultSet set = preparedStatement.executeQuery();
                    if(set.next()) {
                        obj_out.writeUTF("login success");
                        obj_out.flush();
                        int id = set.getInt("id");
                        int role = set.getInt("role");

                    }
                    else {
                        obj_out.writeUTF("login isnt success");
                        obj_out.flush();
                    }


                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
