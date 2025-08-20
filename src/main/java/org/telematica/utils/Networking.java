package org.telematica.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;

public class Networking {
    private static final String ETHERNET_WIFI_INTERFACE = "en1";
    private static final String PRIVATE_IP_RANGE_START = "192";
    
    // Reusable StringBuilder for network interface information
    private static final StringBuilder interfaceBuilder = new StringBuilder(256);
    
    // Connection timeout settings for better resource management
    private static final int CONNECTION_TIMEOUT = 5000; // 5 seconds
    private static final int READ_TIMEOUT = 10000; // 10 seconds
    
    // Cache for public IP to reduce external calls
    private static String cachedPublicIP = null;
    private static long lastIPCacheTime = 0;
    private static final long IP_CACHE_DURATION = 5 * 60 * 1000; // 5 minutes

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static String listLocalNetworkInterfaces(Optional<Boolean> displayAllInterfaces) throws SocketException {
        // Clear and reuse StringBuilder
        interfaceBuilder.setLength(0);
        
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (
                    !Objects.equals(networkInterface.getName(), Networking.ETHERNET_WIFI_INTERFACE) &&
                            (!displayAllInterfaces.isPresent() || displayAllInterfaces.get().equals(Boolean.FALSE))
            ) {
                continue;
            }
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address.getHostAddress().startsWith(PRIVATE_IP_RANGE_START)) {
                    interfaceBuilder
                            .append("Interface name: ")
                            .append(networkInterface.getName())
                            .append(", Address: ")
                            .append(address.getHostAddress())
                            .append("\n");
                }
            }
        }
        return interfaceBuilder.toString();
    }

    public static String getPublicIP() throws IOException {
        // Check if we have a cached IP that's still valid
        long currentTime = System.currentTimeMillis();
        if (cachedPublicIP != null && (currentTime - lastIPCacheTime) < IP_CACHE_DURATION) {
            return cachedPublicIP;
        }
        
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        
        try {
            String serviceURL = "http://checkip.amazonaws.com";
            URL checkIpAws = new URL(serviceURL);
            connection = (HttpURLConnection) checkIpAws.openConnection();
            
            // Set timeouts to prevent hanging connections
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            
            // Check response code before reading
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output = reader.readLine();
            
            // Cache the result
            if (output != null && !output.trim().isEmpty()) {
                cachedPublicIP = output.trim();
                lastIPCacheTime = currentTime;
            }
            
            return cachedPublicIP != null ? cachedPublicIP : "Unknown";
            
        } finally {
            // Ensure proper resource cleanup
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Log but don't throw - we're in cleanup
                    if (!org.telematica.Main.quiet) {
                        System.err.println("Error closing reader: " + e.getMessage());
                    }
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
