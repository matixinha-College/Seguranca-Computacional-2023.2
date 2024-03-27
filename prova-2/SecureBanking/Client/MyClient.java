package Client;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

import Auth.Crypt.MyRSA;
import Auth.Crypt.Util;

public class MyClient implements Runnable {

    private Socket client;
    private boolean connection = true;
    private int clientId;
    private MyRSA rsa = new MyRSA();
    private int seed;
    private BigInteger PublicKey;
    private BigInteger PrivateKey;
    private BigInteger Modulus;

    public MyClient(Socket socket) {
        this.client = socket;
        this.seed = clientId;
        this.PublicKey = rsa.getPublicKey();
        this.PrivateKey = rsa.getPrivateKey();
    }

    @Override
    public void run() {
        try {
            Scanner consoleInput = new Scanner(System.in);
            PrintStream output = new PrintStream(client.getOutputStream());

            if (connection) {
                System.out.println("Conexão estabelecida com sucesso!");
                output.println(PublicKey);
                output.println(seed);
            }

            Thread consoleInputThread = new Thread(() -> {
                try {
                    String userInput;
                    while (connection && consoleInput.hasNextLine()) {
                        userInput = consoleInput.nextLine();
                        if (userInput.equalsIgnoreCase("f")) {
                            connection = false;
                        }
                        // Cifrar a mensagem antes de enviá-la
                        String encryptedMessage = Util.EncryptMessage(userInput);
                        output.println(encryptedMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            consoleInputThread.start();

            Scanner serverInput = new Scanner(client.getInputStream());
            while (connection && serverInput.hasNextLine()) {
                String serverResponse = serverInput.nextLine();
                if (serverResponse == null || serverResponse.equals("Desconectando-se...")) {
                    connection = false;
                    break;
                }
                //System.out.println(serverResponse);
            }

            consoleInput.close();
            serverInput.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
