package de.alshikh.haw.tron.middleware.rpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCServer implements IRPCServer {

    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
    private final HashMap<String, Class<?>> serviceRegistry = new HashMap<>();
    private boolean isRunning = false;

    private final int port;

    public RPCServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException {
        ServerSocket server = new ServerSocket();
        try (server) {
            server.bind(new InetSocketAddress(port));
            System.out.println("start server");
            while (true) {
                executor.execute(send(server.accept()));
            }
        } catch (IOException e) {
            System.out.println("Fatal error");
        }

    }

    private Runnable send(Socket client) {
        return new RPCSender(this, client);
    }

    @Override
    public void register(Class<?> serviceInterface, Class<?> impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    @Override
    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public HashMap<String, Class<?>> getServiceRegistry() {
        return serviceRegistry;
    }
}
