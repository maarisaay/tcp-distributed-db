//import java.io.*;
//import java.net.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class Server {
//
//    private int tcpPort;
//    private ExecutorService executorService;
//
//    public Server (int tcpPort){
//        this.tcpPort = tcpPort;
//        this.executorService = Executors.newCachedThreadPool();
//    }
//
//}

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int portNumber = 12345; // Wybierz numer portu, na którym ma działać serwer

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Tutaj możesz umieścić obsługę klienta w osobnym wątku
                // Na przykład:
                // Thread clientThread = new Thread(new ClientHandler(clientSocket));
                // clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}