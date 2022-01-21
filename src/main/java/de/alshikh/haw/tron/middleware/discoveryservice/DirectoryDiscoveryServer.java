package de.alshikh.haw.tron.middleware.discoveryservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DirectoryDiscoveryServer implements IDiscoveryServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    // TODO: ipv6?
    public static int DISCOVERY_PORT = 4446;
    public static String DISCOVERY_GROUP = "235.0.0.0";

    private final String directoryAddress;
    private final InetAddress multicastGroup;

    public DirectoryDiscoveryServer(InetSocketAddress directoryAddress) {
        this.directoryAddress = directoryAddress.toString();
        try {
            multicastGroup = InetAddress.getByName(DISCOVERY_GROUP);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e); // should not happen but just in case
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
            log.info("Failed to announce " + directoryAddress + " to " + DISCOVERY_GROUP + ":" + DISCOVERY_PORT);
        }
    }

    @Override
    public void announce() throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            log.info("announcing: " + directoryAddress + " to " + multicastGroup + ":" + DISCOVERY_PORT);
            byte[] buf = directoryAddress.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, multicastGroup, DISCOVERY_PORT);
            socket.send(packet);
        }
    }
}
