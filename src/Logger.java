import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final String logFileName;
    private final PrintWriter logWriter;

    public Logger(String logFileName) {
        this.logFileName = logFileName;

        try {
            logWriter = new PrintWriter(new FileWriter(logFileName, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize the logger.", e);
        }
    }

    public void log(String message) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        String logEntry = "[" + formattedTime + "] " + message;
        System.out.println(logEntry);

        // Zapisz log do pliku
        logWriter.println(logEntry);
        logWriter.flush();
    }

    public void close() {
        logWriter.close();
    }
}