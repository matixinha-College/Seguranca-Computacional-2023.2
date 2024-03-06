package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import Auth.Crypt.Util;

public class MyClient implements Runnable {

    private Socket client;
    private boolean connected = true;
    private Util util = new Util();

    public MyClient(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream output = new PrintStream(client.getOutputStream());

            // Thread para lidar com a entrada do usuário no console
            Thread consoleInputThread = new Thread(() -> {
                try {
                    BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                    String userInput;
                    while (connected && (userInput = consoleInput.readLine()) != null) {
                        // Criptografa a mensagem com Base64
                        String messageEncryptedString = util.EncryptMessage(userInput);

                        // Envia a mensagem criptografada para o servidor
                        output.println(messageEncryptedString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            consoleInputThread.start();

            while (connected) {
                // Lê a mensagem do servidor
                String serverResponse = input.readLine();

                // Verifica se o servidor encerrou a conexão
                if (serverResponse == null || serverResponse.equals("Desconectando-se...")) {
                    connected = false;
                    break;
                }
                System.out.println(serverResponse);
            }

            // Fecha os fluxos de entrada e saída e o socket
            input.close();
            output.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}