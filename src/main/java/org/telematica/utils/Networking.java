package org.telematica.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static String listLocalNetworkInterfaces(Optional<Boolean> displayAllInterfaces) throws SocketException {
        StringBuilder interfaces = new StringBuilder();
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
                    interfaces
                            .append("Interface name: ")
                            .append(networkInterface.getName())
                            .append(", Address: ")
                            .append(address.getHostAddress())
                            .append("\n");
                }
            }
        }
        return interfaces.toString();
    }

    public static String getPublicIP() throws IOException {
        String serviceURL = "http://checkip.amazonaws.com";
        URL checkIpAws = new URL(serviceURL);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(checkIpAws.openStream())
        );
        return in.readLine();
    }
}
