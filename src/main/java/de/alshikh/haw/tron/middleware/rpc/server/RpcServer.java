package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.network.RpcConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.alshikh.haw.tron.middleware.rpc.network.util.util.getLocalIp;

public class RpcServer implements IRPCServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    // TODO: managed executor service
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private boolean running = true;
    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry = new HashMap<>();
    private final CompletableFuture<InetSocketAddress> serverAddress = new CompletableFuture<>();

    private final IRpcMessageApi rpcMessageApi;

    public RpcServer(IRpcMessageApi rpcMessageApi) {
        this.rpcMessageApi = rpcMessageApi;
    }

    @Override
    public void register(IRpcAppServerStub serviceServerStub) {
        serviceRegistry.put(serviceServerStub.getServiceId(), serviceServerStub);
    }

    @Override
    public void unregister(UUID serviceId) {
        serviceRegistry.remove(serviceId);
    }

    @Override
    public void start() {
        try (ServerSocket server = new ServerSocket(0, 50, getLocalIp())) {
            serverAddress.complete(new InetSocketAddress(server.getInetAddress(), server.getLocalPort()));
            log.info("RPC server started at port " + server.getLocalPort());
            while (running) {
                executor.execute(newCall(server.accept()));
            }
        } catch (IOException e) {
            log.info("Fatal error: " + e.getMessage());
            log.error("Exception: ", e);
        }
    }

    private Runnable newCall(Socket client) {
        return new ServerStub(new RpcConnection(client), rpcMessageApi, serviceRegistry);
    }

    @Override
    public void stop() {
        running = false;
        executor.shutdown();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        try {
            return serverAddress.get();
        } catch (Exception e) { // TODO:
            return null;
        }
    }

    @Override
    public IRpcMessageApi getMessageApi() {
        return rpcMessageApi;
    }
}
