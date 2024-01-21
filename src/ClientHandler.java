import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DatabaseNode databaseNode;

    public ClientHandler(Socket clientSocket, DatabaseNode databaseNode) {
        this.clientSocket = clientSocket;
        this.databaseNode = databaseNode;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            while (true) {
                // Odbieranie żądania od klienta
                String request = (String) inputStream.readObject();

                // Przetwarzanie żądania i uzyskiwanie odpowiedzi od bazy danych
                String response = databaseNode.processRequest(request);

                // Wysyłanie odpowiedzi do klienta
                outputStream.writeObject(response);
                outputStream.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}