package de.alshikh.haw.tron.middleware.discoveryservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class DirectoryDiscoveryClient implements IDiscoveryClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private String discoveredMsg;
    private final byte[] buf;

    public DirectoryDiscoveryClient() {
        this.discoveredMsg = "";
        this.buf = new byte[256];
    }

    public static InetSocketAddress discover() {
        IDiscoveryClient directoryDiscoveryClient = new DirectoryDiscoveryClient();

        InetSocketAddress discoveredAddress = null;
        while (discoveredAddress == null) {
            directoryDiscoveryClient.listen();
            discoveredAddress = directoryDiscoveryClient.toSocketAddress();
        }

        return discoveredAddress;
    }

    @Override
    public void listen() {
        try (MulticastSocket socket =  new MulticastSocket(DirectoryDiscoveryServer.DISCOVERY_PORT)) {
            InetAddress group = InetAddress.getByName(DirectoryDiscoveryServer.DISCOVERY_GROUP);
            socket.joinGroup(group);

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            discoveredMsg = new String(packet.getData(), 0, packet.getLength());
            log.debug("received message: " + discoveredMsg);

            socket.leaveGroup(group);
        } catch (Exception e) {
            log.info("failed to listen due to network error");
            log.debug("error: ", e);
        }
    }

    @Override
    public InetSocketAddress toSocketAddress() {
        try {
            String[] msg = discoveredMsg.split(":");
            String add = msg[0].replace("/", ""); // TODO
            return new InetSocketAddress(add, Integer.parseInt(msg[1]));
        } catch (Exception e) {
            return null;
        }
    }
}
