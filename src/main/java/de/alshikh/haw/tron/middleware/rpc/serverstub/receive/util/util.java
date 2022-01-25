package de.alshikh.haw.tron.middleware.rpc.serverstub.receive.util;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class util {
    public static int getRandomFreePort() {
        int port = 9000;
        while (true) {
            try (ServerSocket s = new ServerSocket(0, 1, InetAddress.getLocalHost())) {
                return s.getLocalPort();
            } catch (IOException ignored) {}
        }
    }

    // try when possible to get the ip as InetAddress.getLocalHost()
    // sometimes returns the loopback
    public static InetAddress getLocalIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if(Inet4Address.class == addr.getClass())
                        return addr;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return InetAddress.getLoopbackAddress();
    }
}
