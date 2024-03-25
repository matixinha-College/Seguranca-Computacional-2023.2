package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import App.BankService;
import Auth.Crypt.Util;

public class MyServer implements Runnable {

    private Socket socketClient;
    private static int activeConnections = 0;
    private int clientId;
    private boolean connection = true;
    private BankService bankService;

    public MyServer(Socket client, BankService bankService) {
        socketClient = client;
        clientId = clientId + 1;
        this.bankService = bankService;
    }

    public void closeConnection() {
        connection = false;
    }

    @Override
    public void run() {
        System.out.println("\nConexão " + activeConnections + "\n"
                + "Cliente: " + clientId + "\n"
                + "HostAddress: " + socketClient.getInetAddress().getHostAddress() + "\n"
                + "HostName: " + socketClient.getInetAddress().getHostName());

        try {
            PrintStream output = new PrintStream(socketClient.getOutputStream());

            Scanner input = new Scanner(socketClient.getInputStream());
            while (connection && input.hasNextLine()) {
                String messageReceived = input.nextLine();
                if (messageReceived == null) {
                    connection = false;
                    break;
                } else {
                    System.out.println("Dado recebido: Cliente"+ clientId);
                    System.out.println(">> Mensagem cifrada recebida: " + messageReceived);
                    // Decifrar a mensagem antes de processá-la
                    String decryptedMessage = Util.DecryptMessage(messageReceived);
                    System.out.println("Mensagem decifrada: " + decryptedMessage);
                    // Aqui você pode processar a mensagem decifrada, se necessário
                    output.println("Mensagem recebida: " + decryptedMessage);
                }
            }

            input.close();
            output.close();
            System.out.println("Fim do cliente" + clientId + "\n"
                    + socketClient.getInetAddress());
            socketClient.close();
            activeConnections--;
        } catch (Exception e ) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            try {
                System.out.println("Fim do cliente" + clientId + "\n"
                        + socketClient.getInetAddress());
                socketClient.close();
                activeConnections--;
            } catch (IOException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
