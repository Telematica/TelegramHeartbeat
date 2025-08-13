package org.telematica;

import org.telematica.utils.Scheduler;

public class Main {
    public static void main(String[] args) {
        try {
            Scheduler.startNotificatorLoop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Runtime.getRuntime().exit(0);
    }
}