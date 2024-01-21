import java.io.*;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Objects;

public class CommunicationModule implements Runnable {
    private Socket socket;
    private DatabaseNode databaseNode;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public CommunicationModule(Socket socket, DatabaseNode databaseNode) {
        this.socket = Objects.requireNonNull(socket, "Socket cannot be null");
        this.databaseNode = Objects.requireNonNull(databaseNode, "DatabaseNode cannot be null");

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = receiveRequest(); // Odbierz żądanie od klienta

                if (request.equals("terminate")) {
                    // Jeśli klient wysłał żądanie zakończenia, przerwij pętlę
                    break;
                }

                // Przetwórz żądanie i uzyskaj odpowiedź
                String response = processRequest(request);

                // Wyślij odpowiedź do klienta
                sendResponse(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(); // Zamknij połączenie i strumienie danych
        }
    }

    public String receiveRequest() throws IOException, ClassNotFoundException {
        return (String) inputStream.readObject();
    }

    public String processRequest(String request) {
        // Tutaj przetwórz żądanie, na przykład używając swojej metody processRequest
        // i zwróć odpowiedź w formie String
        return "Odpowiedź na żądanie: " + request;
    }

    public void sendResponse(String response) throws IOException {
        outputStream.writeObject(response);
        outputStream.flush();
    }

    public void sendTerminateRequest(OutputStream outputStream) throws IOException {
        this.outputStream.writeObject("terminate");
        this.outputStream.flush();
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendJoinRequest(String ipAddress, int port, int localPort){
        try{
            Socket socket = new Socket(ipAddress, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("JOIN "+ localPort);
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}