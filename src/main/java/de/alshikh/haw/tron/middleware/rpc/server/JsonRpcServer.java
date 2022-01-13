package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackClient;
import de.alshikh.haw.tron.middleware.rpc.client.JsonRpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;
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
import java.util.function.Function;

import static de.alshikh.haw.tron.middleware.rpc.network.util.util.getLocalIp;

public class JsonRpcServer implements IRPCServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    // TODO: managed executor service
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private boolean running = true;
    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry = new HashMap<>();
    private final CompletableFuture<InetSocketAddress> serverAddress = new CompletableFuture<>();

    private final IRpcMessageApi jsonRpcMessageApi;

    public JsonRpcServer() {
        this(new JsonRpcSerializer());
    }

    public JsonRpcServer(JsonRpcSerializer jsonRpcSerializer) {
        this.jsonRpcMessageApi = new JsonRpcMessageApi(jsonRpcSerializer);
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
        // TODO: find a better way
        //  (the server stub should not care about the messaging protocol implementation)
        //  - make a simple RpcServer which is not dependent on the implementation
        Function<Integer, IRpcCallback> newRpcCallback = (port) ->
            new RpcCallbackClient(new JsonRpcClient(
                    new InetSocketAddress(client.getInetAddress(), port),
                    (JsonRpcSerializer) jsonRpcMessageApi.getRpcSerializer()
            ));

        return new ServerStub(client, newRpcCallback, jsonRpcMessageApi, serviceRegistry);
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
        return jsonRpcMessageApi;
    }
}
