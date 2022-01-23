package de.alshikh.haw.tron.middleware.rpc.network;

import de.alshikh.haw.tron.middleware.rpc.network.client.UdpTcpClient;
import de.alshikh.haw.tron.middleware.rpc.network.server.UdpTcpServer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        UdpTcpServer udpTcpServer = new UdpTcpServer(0);
        new Thread(udpTcpServer::start).start();

        UdpTcpClient udpTcpClient = new UdpTcpClient(udpTcpServer.getAddress().get());

        String msg = "Quick Test";

        for (int i = 0; i < 50; i++) {
            byte[] data = (msg + i).getBytes(StandardCharsets.UTF_8);
            executor.submit(() -> udpTcpClient.send(data, true));
            executor.submit(() -> udpTcpClient.send(data, false));
        }
    }
}
