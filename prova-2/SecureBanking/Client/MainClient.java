package Client;

import java.net.Socket;

public class MainClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            MyClient client = new MyClient(socket);
            Thread thread = new Thread(client);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}