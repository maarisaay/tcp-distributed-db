
import java.io.IOException;
import java.net.Socket;

public class TestMultiClient {
    public static void main(String[] args){
        int numberOfClients = 5;
        DatabaseNode databaseNode = new DatabaseNode(12345);

        for(int i = 0; i < numberOfClients; i++){
            try{
                Socket clientSocket = new Socket("localhost", 12345);
                Thread clientThread = new Thread(new ClientHandler(clientSocket, databaseNode));
                clientThread.start();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        try{
            for (int i = 0; i < numberOfClients; i++){
                Thread clientThread = new Thread(new ClientHandler(null, databaseNode));
                clientThread.join();
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        databaseNode.terminate();

    }
}
