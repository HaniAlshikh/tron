package de.alshikh.haw.tron.middleware.directoryserver.discovery;

import de.alshikh.haw.tron.Config;
import de.alshikh.haw.tron.middleware.discoveryservice.IDiscoveryServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// TODO: ipv6?
public class DirectoryDiscoveryServer implements IDiscoveryServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final String directoryAddress;
    private final InetAddress multicastGroup;

    public DirectoryDiscoveryServer(InetSocketAddress directoryAddress) {
        this.directoryAddress = directoryAddress.toString();
        try {
            multicastGroup = InetAddress.getByName(Config.DISCOVERY_GROUP);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExecutorService multicast(InetSocketAddress directoryAddress, int period, TimeUnit unit) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        DirectoryDiscoveryServer ds = new DirectoryDiscoveryServer(directoryAddress);
        executor.scheduleAtFixedRate(ds, 0, period, unit);
        return executor;
    }

    @Override
    public void run() {
        try {
            announce();
        } catch (Exception e) {
            log.info("Failed to announce " + directoryAddress + " to " + Config.DISCOVERY_GROUP + ":" + Config.DISCOVERY_PORT);
        }
    }

    @Override
    public void announce() throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            log.info("announcing: " + directoryAddress + " to " + multicastGroup + ":" + Config.DISCOVERY_PORT);
            byte[] buf = directoryAddress.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, multicastGroup, Config.DISCOVERY_PORT);
            socket.send(packet);
        }
    }
}
