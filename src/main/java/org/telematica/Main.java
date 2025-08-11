package org.telematica;

import org.telematica.utils.Networking;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static Set<String> publicIPHistory = new HashSet<>();
    private static Set<String> localIPHistory = new HashSet<>();
    public static void main(String[] args) {
        /*
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Your code to be executed at each interval
                System.out.println("Task executed at interval!");
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000); // Execute every 1 second (1000 milliseconds)
        */
        try {
            System.out.println(Networking.getPublicIP());
            Networking.listLocalNetworkInterfaces(Optional.of(false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().exit(0);
    }
}