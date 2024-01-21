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

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
//                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                String response = databaseNode.processRequest(request);
                out.println(response);
//
//                // Odbieranie żądania od klienta
//                String request = (String) inputStream.readObject();
//
//                // Przetwarzanie żądania i uzyskiwanie odpowiedzi od bazy danych
//                String response = databaseNode.processRequest(request);
//
//                // Wysyłanie odpowiedzi do klienta
//                outputStream.writeObject(response);
//                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void handleClientRequest() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String request;
            while ((request = reader.readLine()) != null) {
                // Obsługa żądania klienta
                String response = processRequest(request);

                // Wysłanie odpowiedzi do klienta
                writer.write(response + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processRequest(String request) {
        String response = databaseNode.processRequest(request);
        return response;
    }
}