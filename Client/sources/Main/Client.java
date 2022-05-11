package Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Client extends Thread {


    ObjectOutputStream out;
    ObjectInputStream in;
    static Socket socket;
    public boolean isGood = false;


    public void enable() {
        isGood = true;
        System.out.println("enabled");
    }






    @Override
    public void run() {
        super.run();
        while(isGood) {
            try {
                socket = new Socket("127.0.0.1", 8080);
                try(ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objInput = new ObjectInputStream(socket.getInputStream())) {
                    this.out = objOut;
                    this.in = objInput;
                    while(!socket.isOutputShutdown() && isGood) {

                    }
                    isGood = false;
                }
                catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
            catch (Exception exception) {
                System.out.println(exception.getMessage());
                try {
                    Thread.sleep(1000);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public String Login(String e_mail, String passwd) throws IOException, InterruptedException {
        System.out.println("login...");
        String client_say = "login";
        out.writeUTF(client_say);
        out.flush();
        out.writeUTF(e_mail + " " + passwd);
        out.flush();
        Thread.sleep(500);
        String msg = in.readUTF();
        if(msg.equalsIgnoreCase("login success")) {
            System.out.println("login success");
            return msg;
        }
        else return "-1";
    }

    public String Register(String e_mail, String passwd, String passwd2) throws IOException, InterruptedException {
        String client_say = "register";
        out.writeUTF(client_say);
        out.flush();
        out.writeUTF(e_mail + " " + passwd + " " + passwd2);
        out.flush();
        Thread.sleep(500);
        String msg = in.readUTF();
        if(msg.equalsIgnoreCase("register success")) {
            System.out.println("register success!");
            return msg;
        }
        else return "-1";

    }





}
