package org.telematica.utils;

import org.telematica.requests.TelegramEditMessageText;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class Scheduler {
    private static final long PERIOD = 60 * 1000;

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
                System.gc();
            }
        };
        timer.scheduleAtFixedRate(task, 0, Scheduler.PERIOD);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void startNotificatorLoop() {
        while (true) {
            try {
                String message =
                        "Alive Date: " + new Date() + "\n" + "\n" +
                        "Public IP: " + Networking.getPublicIP() + "\n" + "\n" +
                        Networking.listLocalNetworkInterfaces(Optional.of(false)) + "\n" +
                        "PID: " + Management.PID;
                System.out.println(message);
                TelegramEditMessageText.send(
                        System.getenv().getOrDefault("JAVA_TELEGRAM_USER_ID", ""),
                        System.getenv().getOrDefault("JAVA_TELEGRAM_MESSAGE_ID", ""),
                        message,
                        false
                );
                sleep(PERIOD);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
