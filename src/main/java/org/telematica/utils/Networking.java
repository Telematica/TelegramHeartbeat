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
    private static final String ETHERNET_WIFI_INTERFACE = "en0";

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static void listLocalNetworkInterfaces(Optional<Boolean> displayAllInterfaces) throws SocketException {
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = e.nextElement();
            if (
                    !Objects.equals(n.getName(), Networking.ETHERNET_WIFI_INTERFACE) &&
                            (!displayAllInterfaces.isPresent() || displayAllInterfaces.get().equals(Boolean.FALSE))
            ) {
                continue;
            }
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = ee.nextElement();
                System.out.println("Interface name: " + n.getName() + ", Address: " + i.getHostAddress());
            }
        }
    }

    public static String getPublicIP() throws IOException {
        String serviceURL = "http://checkip.amazonaws.com";
        URL checkIPAWS = new URL(serviceURL);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(checkIPAWS.openStream())
        );
        return in.readLine();
    }
}
