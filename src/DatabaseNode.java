import org.w3c.dom.Node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class DatabaseNode {
    private int tcpPort;
    private Map<Integer, Integer> database;
    private List<NodeInfo> connectedNodes;
    private ExecutorService executorService;
    private ObjectOutputStream outputStream;
    private CommunicationModule communicationModule;
    private Logger logger;
    private ServerSocket serverSocket;
    private final Object databaseLock = new Object();
    private static final int initialKey = 1;
    private static final int initialValue = 100;
    private static final List<NodeInfo> initialNodes = Arrays.asList(
            new NodeInfo("localhost", 12345),
            new NodeInfo("127.0.0.1", 54321));

    public DatabaseNode(int tcpPort) {
        this.tcpPort = tcpPort;
        this.database = new HashMap<>();
        this.connectedNodes = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.outputStream = null;
        this.logger = Logger.getLogger(DatabaseNode.class.getName());
        this.database.put(initialKey, initialValue);
        this.connectedNodes.addAll(initialNodes);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
            System.out.println("Node is listening on port " + tcpPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Utwórz obiekt DatabaseNode z argumentami
                DatabaseNode databaseNode = new DatabaseNode(tcpPort);

                // Obsługa połączenia w osobnym wątku
                Thread clientThread = new Thread(new ClientHandler(clientSocket, databaseNode));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Przykładowa implementacja operacji set-value
    public void setValue(int key, int value) {
        database.put(key, value);
        System.out.println("Set key " + key + " to value " + value);
    }

    // Przykładowa implementacja operacji get-value
    public int getValue(int key) {
        return database.getOrDefault(key, -1);
    }

    // Implementacja operacji find-key
    public String findKey(int key) {
        if (database.containsKey(key)) {
            return "FOUND";
        } else {
            return "NOT FOUND";
        }
    }

    // Implementacja operacji get-max
    public int getMax() {
        int maxKey = -1;
        int maxValue = Integer.MIN_VALUE;

        for (Map.Entry<Integer, Integer> entry : database.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }

        return maxKey;
    }

    // Implementacja operacji get-min
    public int getMin() {
        int minKey = -1;
        int minValue = Integer.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : database.entrySet()) {
            if (entry.getValue() < minValue) {
                minKey = entry.getKey();
                minValue = entry.getValue();
            }
        }

        return minKey;
    }

    // Implementacja operacji new-record
    public void newRecord(int key, int value) {
        database.put(key, value);
        System.out.println("Added new record: Key=" + key + ", Value=" + value);
    }

    public String processRequest(String request) {
        String[] parts = request.split(" ");
        String operation = parts[0];
        String[] params = Arrays.copyOfRange(parts, 1, parts.length);

        try {
            switch (operation) {
                case "set-value":
                    int key = Integer.parseInt(params[0]);
                    int value = Integer.parseInt(params[1]);
                    synchronized (databaseLock) {
                        database.put(key, value);
                    }
                    return "OK";
                case "get-value":
                    int queryKey = Integer.parseInt(params[0]);
                    int queryValue;
                    synchronized (databaseLock) {
                        queryValue = database.getOrDefault(queryKey, -1);
                    }
                    return queryKey + ":" + queryValue;
                case "find-key":
                    int searchKey = Integer.parseInt(params[0]);
                    NodeInfo nodeWithKey;
                    synchronized (databaseLock) {
                        nodeWithKey = findNodeForKey(searchKey);
                    }
                    if (nodeWithKey != null) {
                        return nodeWithKey.getIp() + ":" + nodeWithKey.getPort();
                    } else {
                        return "ERROR";
                    }
                case "get-max":
                    int maxKey;
                    int maxValue;
                    synchronized (databaseLock) {
                        maxKey = Collections.max(database.keySet());
                        maxValue = database.get(maxKey);
                    }
                    return maxKey + ":" + maxValue;
                case "get-min":
                    int minKey;
                    int minValue;
                    synchronized (databaseLock) {
                        minKey = Collections.min(database.keySet());
                        minValue = database.get(minKey);
                    }
                    return minKey + ":" + minValue;
                case "new-record":
                    int newRecordKey = Integer.parseInt(params[0]);
                    int newRecordValue = Integer.parseInt(params[1]);
                    synchronized (databaseLock) {
                        database.put(newRecordKey, newRecordValue);
                    }
                    return "OK";
                case "terminate":
                    disconnectFromNodes();
                    return "OK";
                default:
                    return "ERROR: Invalid operation";
            }
        } catch (NumberFormatException e) {
            return "ERROR: Invalid parameters";
        }
    }

    private NodeInfo findNodeForKey (int key){
        int numberOfNodes = connectedNodes.size();
        int nodeIndex = key % numberOfNodes;
        return connectedNodes.get(nodeIndex);
    }
    private void disconnectFromNodes(){
        for (NodeInfo nodeInfo : connectedNodes){
            try{
                Socket socket = new Socket(nodeInfo.getIp(), nodeInfo.getPort());
                communicationModule.sendTerminateRequest(socket.getOutputStream());
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        connectedNodes.clear();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java DatabaseNode <tcpPort>");
            System.exit(1);
        }

        int tcpPort = Integer.parseInt(args[0]);
        DatabaseNode databaseNode = new DatabaseNode(tcpPort);
        databaseNode.start();
    }
}
