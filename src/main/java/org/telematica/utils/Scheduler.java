package org.telematica.utils;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.telematica.Main;
import org.telematica.requests.TelegramEditMessageText;

public class Scheduler {
    private static final long PERIOD = 60 * 1000;
    
    // Reusable objects to avoid creating new instances every iteration
    private static final StringBuilder messageBuilder = new StringBuilder(512);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Runtime runtime = Runtime.getRuntime();

    // Memory monitoring
    private static long lastMemoryLog = 0;
    private static final long MEMORY_LOG_INTERVAL = 2 * 60 * 1000; // Log memory every 2 minutes

    public static void startNotificatorTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Public IP: " + Networking.getPublicIP());
                    Networking.listLocalNetworkInterfaces(Optional.of(false));
                    System.out.println(new Date());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Remove manual GC call - let JVM handle it automatically
            }
        };
        timer.scheduleAtFixedRate(task, 0, Scheduler.PERIOD);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void startNotificatorLoop() {
        String userId = System.getenv().getOrDefault("JAVA_TELEGRAM_USER_ID", "");
        String messageId = System.getenv().getOrDefault("JAVA_TELEGRAM_MESSAGE_ID", "");
        long processId = Management.PID;
        
        // Pre-allocate frequently used strings
        final String aliveDatePrefix = "Alive Date: ";
        final String publicIpPrefix = "Public IP: ";
        final String pidPrefix = "PID: ";
        final String newLine = "\n";
        
        while (true) {
            try {
                // Log memory usage periodically instead of every iteration
                long currentTime = System.currentTimeMillis();

                // Clear and reuse StringBuilder instead of creating new strings
                messageBuilder.setLength(0);
                
                // Build message efficiently using StringBuilder
                messageBuilder.append(aliveDatePrefix)
                           .append(dateFormat.format(new Date()))
                           .append(newLine).append(newLine)
                           .append(publicIpPrefix)
                           .append(Networking.getPublicIP())
                           .append(newLine).append(newLine)
                           .append(Networking.listLocalNetworkInterfaces(Optional.of(false)))
                           .append(newLine)
                           .append(pidPrefix)
                           .append(processId)
                           .append(newLine).append(newLine)
                           .append((currentTime - lastMemoryLog > MEMORY_LOG_INTERVAL) ? logMemoryUsage() : "");
                
                String message = messageBuilder.toString();
                
                if (!Main.quiet) {
                    System.out.println(message);
                }

                if (currentTime - lastMemoryLog > MEMORY_LOG_INTERVAL) {
                    lastMemoryLog = currentTime;
                }
                
                TelegramEditMessageText.send(userId, messageId, message);

                // Remove manual GC call - let JVM handle it automatically
                sleep(PERIOD);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Logs current memory usage for monitoring purposes
     */
    private static String logMemoryUsage() {
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        return String.format(
                "Memory Usage \nUsed: %d MB \nFree: %d MB \nTotal: %d MB \nMax: %d MB%n",
                usedMemory / (1024 * 1024),
                freeMemory / (1024 * 1024),
                totalMemory / (1024 * 1024),
                maxMemory / (1024 * 1024)
        );
    }
}
