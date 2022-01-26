package de.alshikh.haw.tron.middleware.directoryserver.discovery;

import de.alshikh.haw.tron.Config;
import de.alshikh.haw.tron.middleware.discoveryservice.IDiscoveryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

public class DirectoryDiscoveryClient implements IDiscoveryClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private String discoveredMsg;

    public DirectoryDiscoveryClient() {}

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
        try (MulticastSocket socket =  new MulticastSocket(Config.DISCOVERY_PORT)) {
            InetAddress group = InetAddress.getByName(Config.DISCOVERY_GROUP);
            socket.joinGroup(group);

            byte[] buf = new byte[256];
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
            String add = msg[0].replace("/", "");
            return new InetSocketAddress(add, Integer.parseInt(msg[1]));
        } catch (Exception e) {
            return null;
        }
    }
}
