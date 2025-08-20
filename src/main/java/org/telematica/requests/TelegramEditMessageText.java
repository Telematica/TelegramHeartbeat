package org.telematica.requests;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TelegramEditMessageText {
    // Connection timeout settings for better resource management
    private static final int CONNECTION_TIMEOUT = 5000; // 5 seconds
    private static final int READ_TIMEOUT = 10000; // 10 seconds
    
    // Reusable map to reduce object creation
    private static final Map<String, Object> paramsMap = new HashMap<>(4);
    
    public static void send(String chatId, String messageId, String text) {
        HttpURLConnection connection = null;
        
        try {
            // Clear and reuse the params map
            paramsMap.clear();
            paramsMap.put("chat_id", chatId);
            paramsMap.put("message_id", messageId);
            paramsMap.put("text", text);
            
            String queryParams = org.telematica.utils.Uri.toQueryParam(paramsMap);
            URL url = new URL(
                    System.getenv("JAVA_TELEGRAM_API_URL") +
                    System.getenv("JAVA_TELEGRAM_API_KEY") + "/editMessageText" + "?" +
                    queryParams
            );
            
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            
            // Set timeouts to prevent hanging connections
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            
            // Get response code to ensure the request was processed
            int responseCode = connection.getResponseCode();
            
            // Log response for debugging (only in non-quiet mode)
            if (!org.telematica.Main.quiet && responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Telegram API response code: " + responseCode);
            }
            
        } catch (Exception e) {
            // Log the error but don't throw RuntimeException to avoid crashing the main loop
            if (!org.telematica.Main.quiet) {
                System.err.println("Error sending Telegram message: " + e.getMessage());
            }
        } finally {
            // Ensure proper resource cleanup
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    // Log but don't throw - we're in cleanup
                    if (!org.telematica.Main.quiet) {
                        System.err.println("Error disconnecting: " + e.getMessage());
                    }
                }
            }
        }
    }
}
