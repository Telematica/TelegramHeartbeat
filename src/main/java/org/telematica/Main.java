package org.telematica;

import org.telematica.utils.Scheduler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static boolean quiet = false;
    public static void main(String[] args) {
        try {
            Set<String> arguments = new HashSet<>(Arrays.asList(args));
            Main.quiet = arguments.contains("--quiet");
            Scheduler.startNotificatorLoop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Runtime.getRuntime().exit(0);
    }
}