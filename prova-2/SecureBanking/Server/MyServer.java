package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import App.BankService;
import Auth.Crypt.MyRSA;
import Auth.Crypt.Util;

public class MyServer implements Runnable {

    private Socket socketClient;
    private static int activeConnections = 0;
    private int clientId;
    private boolean connection = true;
    private BankService bankService;
    private MyRSA rsa = new MyRSA();
    private int seed;
    private BigInteger PublicKey;
    private BigInteger PrivateKey;
    private Map<Integer, BigInteger> ClientPublicKey = new HashMap<>();

    public MyServer(Socket client, BankService bankService) {
        socketClient = client;
        clientId = client.getPort();
        this.bankService = bankService;
        this.seed = clientId;
        this.PublicKey = rsa.getPublicKey();
        this.PrivateKey = rsa.getPrivateKey();
    }

    @Override
    public void run() {
        try {

            PrintStream output = new PrintStream(socketClient.getOutputStream());
            Scanner input = new Scanner(socketClient.getInputStream());
            ClientPublicKey.put(clientId, new BigInteger(input.nextLine()));
            output.println(seed);
            System.out.println("\nConexão " + activeConnections);
            System.out.println("Cliente: " + clientId + "\n");
            System.out.println("Chave pública do cliente: " + ClientPublicKey.get(clientId));
            System.out.println("Chave pública do servidor: " + PublicKey);
            System.out.println("Chave privada do servidor: " + PrivateKey);

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            PrintStream output = new PrintStream(socketClient.getOutputStream());
            Scanner input = new Scanner(socketClient.getInputStream());
            while (connection && input.hasNextLine()) {
                String messageReceived = input.nextLine();
                if (messageReceived == null) {
                    connection = false;
                    break;
                } else {
                    System.out.println("Dado recebido: Cliente" + clientId);
                    System.out.println(">> Mensagem cifrada recebida: " + messageReceived);

                    String decryptedMessage = Util.DecryptMessage(messageReceived);
                    System.out.println("Mensagem decifrada: " + decryptedMessage);

                    output.println("Mensagem recebida: " + decryptedMessage);
                }
            }

            input.close();
            output.close();
            System.out.println("Fim do cliente" + clientId + "\n"
                    + socketClient.getInetAddress());
            socketClient.close();
            activeConnections--;
        } catch (Exception e) {
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
