public class TestSingleNodes {
    public static void main(String[] args) {
        // Tworzymy węzły i uruchamiamy je
        DatabaseNode node1 = new DatabaseNode(12345);
        DatabaseNode node2 = new DatabaseNode(54321);

        // Uruchamiamy węzły w osobnych wątkach
        Thread thread1 = new Thread(() -> node1.start());
        Thread thread2 = new Thread(() -> node2.start());

        thread1.start();
        thread2.start();

        // Testujemy funkcjonalność węzłów
        try {
            Thread.sleep(1000);

            String request1 = "set-value 1:100";
            String request2 = "get-value 1";
            String request3 = "find-key 2";
            String request4 = "get-max";
            String request5 = "get-min";
            String request6 = "new-record 3:200";

            System.out.println("Request 1: " + node1.processRequest(request1));
            System.out.println("Request 2: " + node2.processRequest(request2));
            System.out.println("Request 3: " + node1.processRequest(request3));
            System.out.println("Request 4: " + node1.processRequest(request4));
            System.out.println("Request 5: " + node1.processRequest(request5));
            System.out.println("Request 6: " + node1.processRequest(request6));

            node1.terminate();
            node2.terminate();

            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
