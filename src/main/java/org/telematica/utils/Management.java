package org.telematica.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Management {
    public static long PID = Management.getPID();

    private static long getPID() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        String jvmName = runtimeBean.getName();

        // The name string is typically in the format "pid@hostname"
        // Extract the PID by splitting the string at '@' and taking the first part.
        // System.out.println("JVM Name: " + jvmName);
        // System.out.println("Process ID (PID): " + pid);
        return Long.parseLong(jvmName.split("@")[0]);
    }
}
