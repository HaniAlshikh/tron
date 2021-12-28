package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcServiceServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessage;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessage;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsonRpcServer implements IRPCServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IRpcMessage jsonRpcMessage;
    private final HashMap<String, IRpcServiceServerStub> serviceRegistry = new HashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private boolean running = true;

    private final int port;

    public JsonRpcServer(int port) {
        this(port, new JsonRpcSerializer());
    }

    public JsonRpcServer(int port, JsonRpcSerializer jsonRpcSerializer) {
        this.port = port;
        this.jsonRpcMessage = new JsonRpcMessage(jsonRpcSerializer);
    }

    @Override
    public void register(Class<?> serviceInterface, IRpcServiceServerStub serviceServerStub) {
        serviceRegistry.put(serviceInterface.getSimpleName(), serviceServerStub);
    }

    @Override
    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(port));
            log.info("RPC server started");
            while (running) {
                executor.execute(newCall(server.accept()));
            }
        } catch (IOException e) {
            log.info("Fatal error: " + e.getMessage());
            log.error("Exception: ", e);
        }
    }

    private Runnable newCall(Socket client) {
        return new ServerStub(client, jsonRpcMessage, serviceRegistry);
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
    public int getPort() {
        return port;
    }
}
