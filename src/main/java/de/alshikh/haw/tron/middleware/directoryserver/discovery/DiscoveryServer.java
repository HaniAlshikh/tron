package de.alshikh.haw.tron.middleware.directoryserver.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscoveryServer implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    // TODO: ipv6?
    public static int DISCOVERY_PORT = 4446;
    public static String DISCOVERY_GROUP = "235.0.0.0";

    private final String message;
    private InetAddress group;

    public DiscoveryServer(String message) {
        this.message = message;
        try {
            group = InetAddress.getByName(DISCOVERY_GROUP);
        } catch (UnknownHostException ignored) {} // TODO
    }

    public static ExecutorService multicast(String message, int period) {
        // TODO: managed executor service
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        DiscoveryServer ds = new DiscoveryServer(message);
        executor.scheduleAtFixedRate(ds, 0, period, TimeUnit.SECONDS);
        return executor;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            log.info("announcing: " + message + " to " + group + ":" + DISCOVERY_PORT);
            byte[] buf = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, DISCOVERY_PORT);
            socket.send(packet);
        } catch (Exception e) {
            log.info("Failed to announce " + message + " to " + DISCOVERY_GROUP + ":" + DISCOVERY_PORT);
        }
    }

}
