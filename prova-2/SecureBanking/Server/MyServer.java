package Server;

import java.net.Socket;
import java.util.Scanner;

public class MyServer implements Runnable {

    public Socket socketClient; 
    private static int activeConnections = 0; // número de conexões ativas
    private int clientId; // ID único para cada cliente
    private boolean connection = true;

    public MyServer(Socket client){
        socketClient = client;
        clientId = ++activeConnections; // Incrementa o contador e atribui o ID do cliente
    }

    @Override
    public void run() {
        System.out.println("\nConexão " + activeConnections + "\n"
                + "Cliente: " + clientId + "\n"
                + "HostAddress: " + socketClient.getInetAddress().getHostAddress() + "\n" 
                + "HostName: " + socketClient.getInetAddress().getHostName());
        

        try {

            Scanner scan = new Scanner(socketClient.getInputStream());

            while(connection){
                String messageC = scan.nextLine();

                if(messageC.equalsIgnoreCase("f")){
                    connection=false;
                }else{
                    System.out.println("\nConexões: " + activeConnections + "\nCliente " + clientId + ": " + messageC);
                }
            }

            //Fecha scanner e socket
            scan.close();
            System.out.println("\nFim da conexão do cliente " + clientId + "\n"
                    + socketClient.getInetAddress().getHostAddress() + "\n"
                    + socketClient.getInetAddress().getHostName());

            // Decrementa o número de conexões ativas
            activeConnections--;

            socketClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
