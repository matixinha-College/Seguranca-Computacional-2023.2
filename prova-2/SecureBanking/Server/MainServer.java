package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import App.BankService;
import Auth.AuthenticationService;

public class MainServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor iniciado. Aguardando conexões...");
            AuthenticationService authService = new AuthenticationService();
            BankService bankService = new BankService(authService);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + socket.getInetAddress());

                Thread t = new Thread(new MyServer(socket, bankService));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
