package org.telematica.requests;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class TelegramEditMessageText {
    public static void send(String chatId, String messageId, String text, Boolean disableNotification) {
        HttpURLConnection connection = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            params.put("message_id", messageId);
            params.put("text", text);
            params.put("disableNotification", disableNotification);
            String queryParams = org.telematica.utils.Uri.toQueryParam(params);
            URL url = new URL(
                    System.getenv("JAVA_TELEGRAM_API_URL") +
                    System.getenv("JAVA_TELEGRAM_API_KEY") + "/editMessageText" + "?" +
                    queryParams
            );
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.getResponseCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
