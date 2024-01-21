import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestCommunicationModule {
    public static void main(String[] args) {
        int serverPort = 12345; // Domyślny port serwera
        DatabaseNode databaseNode = new DatabaseNode(serverPort); // Tworzenie instancji DatabaseNode

        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is listening on port " + serverPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                CommunicationModule communicationModule = new CommunicationModule(clientSocket, databaseNode);

                // Uruchom wątek CommunicationModule
                Thread clientThread = new Thread(communicationModule);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}