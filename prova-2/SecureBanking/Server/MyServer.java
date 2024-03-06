package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import Auth.Crypt.Util;
import App.BankService;

public class MyServer implements Runnable {

    private Socket socketClient; 
    private static int activeConnections = 0; // número de conexões ativas
    private int clientId; // ID único para cada cliente
    //private boolean connection = true;
    private BankService bankService;
    private Util util = new Util();

    public MyServer(Socket client, BankService bankService){
        socketClient = client;
        clientId = ++activeConnections; // Incrementa o contador e atribui o ID do cliente
        this.bankService = bankService;
    }

    @Override
    public void run() {
        System.out.println("\nConexão " + activeConnections + "\n"
                + "Cliente: " + clientId + "\n"
                + "HostAddress: " + socketClient.getInetAddress().getHostAddress() + "\n" 
                + "HostName: " + socketClient.getInetAddress().getHostName());

        try {
            // Cria um BufferedReader para ler do cliente
            BufferedReader input = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            // Cria um PrintStream para enviar dados para o cliente
            PrintStream output = new PrintStream(socketClient.getOutputStream());

            String encryptedMessage = input.readLine();

            if (encryptedMessage != null && encryptedMessage.length() > 0) {
                // Decifra a mensagem
                String decryptedMessage = util.DecryptMessage(encryptedMessage);
            
                // Imprime a mensagem decifrada
                System.out.println("Mensagem decifrada: " + decryptedMessage);
            
                // Lógica do banco de dados com a mensagem decifrada
                bankService.handleClient(util.DecryptMessage(input.readLine()), output);
            } else {
                System.out.println("Mensagem recebida vazia ou inválida.");
            }

            // Fecha os fluxos de entrada e saída e o socket
            input.close();
            output.close();
            socketClient.close();

            // Decrementa o número de conexões ativas
            activeConnections--;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
