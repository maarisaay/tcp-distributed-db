public class TestDatabaseNode {
    public static void main(String[] args) {
        DatabaseNode databaseNode = new DatabaseNode(12345);

        // Przykładowe komunikaty
        String request1 = "set-value 1 100";
        String request2 = "get-value 1";
        String request3 = "find-key 2";
        String request4 = "get-max";
        String request5 = "get-min";
        String request6 = "new-record 3 200";
        String request7 = "terminate";

        // Testowanie funkcji processRequest
        System.out.println(databaseNode.processRequest(request1));
        System.out.println(databaseNode.processRequest(request2));
        System.out.println(databaseNode.processRequest(request3));
        System.out.println(databaseNode.processRequest(request4));
        System.out.println(databaseNode.processRequest(request5));
        System.out.println(databaseNode.processRequest(request6));
        System.out.println(databaseNode.processRequest(request7));
    }
}
