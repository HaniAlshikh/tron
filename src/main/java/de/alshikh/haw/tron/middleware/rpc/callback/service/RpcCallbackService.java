package de.alshikh.haw.tron.middleware.rpc.callback.service;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.stubs.RpcCallbackClient;
import de.alshikh.haw.tron.middleware.rpc.client.RpcClient;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RpcCallbackService implements IRpcCallbackService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    private final ConcurrentMap<UUID, IRpcCallbackHandler> responseRegistry;
    // TODO: is this the right place?
    private IRpcMarshaller rpcMarshaller;


    private static final RpcCallbackService instance = new RpcCallbackService();
    public static RpcCallbackService getInstance() {
        return instance;
    }
    private RpcCallbackService() {
        responseRegistry = new ConcurrentHashMap<>();
    }

    @Override
    public void register(UUID requestId, IRpcCallbackHandler callback) {
        responseRegistry.put(requestId, callback);
    }

    @Override
    public void setResponse(UUID requestId, Object response) {
        responseRegistry.get(requestId).complete(response);
    }

    @Override
    public IRpcCallback newRpcCallback(InetAddress receiverAddress, int port) {
        // TODO: what is the correct way to create the callback client?
        if (rpcMarshaller == null)
            throw new RuntimeException("missing rpc marshaller to create a callback");

        return new RpcCallbackClient(new RpcClient(
                        new InetSocketAddress(receiverAddress, port),
                        rpcMarshaller
                ));
    }

    @Override
    public IRpcMarshaller getRpcMarshaller() {
        return rpcMarshaller;
    }

    @Override
    public void setRpcMarshaller(IRpcMarshaller rpcMarshaller) {
        this.rpcMarshaller = rpcMarshaller;
    }
}
