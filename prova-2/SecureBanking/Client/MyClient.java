package Client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class MyClient implements Runnable {
    private Socket client;
    private boolean connection = true;
    private PrintStream outputC;

    public String clientN;
    public MyClient(Socket c){
        this.client = c;
    }
    @Override
    public void run() {
        try {
            System.out.println("Cliente logado");

            Scanner input = new Scanner(System.in);
            
            //Receber mensagem do servidor
            //messageS = new Scanner(client.getInputStream());
            
            //Enviar mensagem ao servidor   
            outputC = new PrintStream(client.getOutputStream());
            String messageC;

            

            while(connection){
                System.out.println("Conexão bem sucedida");
                System.out.println("Mensagem:");

                messageC = input.nextLine();

                if(messageC.equalsIgnoreCase("f")){
                    connection = false;
                }else{
                    System.out.println("Mensagem enviada com sucesso\n");
                }
                outputC.println(messageC);
            }

            outputC.close();
            input.close();
            client.close();
            System.out.println("F parte 2");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}