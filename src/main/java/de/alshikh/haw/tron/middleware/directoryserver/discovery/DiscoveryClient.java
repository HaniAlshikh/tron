package de.alshikh.haw.tron.middleware.directoryserver.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class DiscoveryClient {
    private static final Logger log = LoggerFactory.getLogger(DiscoveryClient.class.getSimpleName());

    private final byte[] buf = new byte[256];

    public DiscoveryClient() {}

    public static InetSocketAddress getServerAddress() {
        InetSocketAddress discoveredAddress = null;
        DiscoveryClient discoveryClient = new DiscoveryClient();
        while (discoveredAddress == null) {
            String msg = discoveryClient.receive();
            log.debug("received message: " + msg);
            discoveredAddress = toSocketAddress(msg);
        }
        return discoveredAddress;
    }

    public static InetSocketAddress toSocketAddress(String message) {
        try {
            String[] msg = message.split(":");
            String add = msg[0].replace("/", ""); // TODO
            return new InetSocketAddress(add, Integer.parseInt(msg[1]));
        } catch (Exception e) {
            return null;
        }
    }

    private String receive() {
        try (MulticastSocket socket =  new MulticastSocket(DiscoveryServer.DISCOVERY_PORT)) {
            InetAddress group = InetAddress.getByName(DiscoveryServer.DISCOVERY_GROUP);
            socket.joinGroup(group);

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            socket.leaveGroup(group);
            return new String(packet.getData(), 0, packet.getLength());
        } catch (Exception e) {
            log.info("Failed to receive any messages");
            log.debug("error: ", e);
        }
        return "";
    }
}
