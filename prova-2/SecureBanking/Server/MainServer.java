package Server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer {

    public static void main(String[] args) throws Exception{

        //Cria um socket na porta 12345
        ServerSocket serverSocket = new ServerSocket(12345);

        System.out.println("\nServer port: " + serverSocket.getLocalPort());
        System.out.println("HostAddress: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("HostName: " + InetAddress.getLocalHost().getHostName());

        System.out.println("\nAguardando conexão do cliente");
    
        while(true){
            Socket client = serverSocket.accept();
            
            //Cria uma thread do servidor para tratar a conexão
            MyServer server = new MyServer(client);
            Thread t = new Thread(server);

            //Inicia a thread para o client conectado
            t.start();
        }
    }

}
