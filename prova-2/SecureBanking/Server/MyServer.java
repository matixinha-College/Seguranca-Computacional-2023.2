package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import App.BankService;

public class MyServer implements Runnable {

    private Socket socketClient; 
    private static int activeConnections = 0; // número de conexões ativas
    private int clientId; // ID único para cada cliente
    private boolean connection = true;
    private BankService bankService;

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

            bankService.handleClient(input, output);

            // Fecha os fluxos de entrada e saída e o socket
            input.close();
            output.close();
            socketClient.close();

            // Decrementa o número de conexões ativas
            activeConnections--;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}