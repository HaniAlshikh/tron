package de.alshikh.haw.tron.middleware.rpc.callback.service;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RpcCallbackService implements IRpcCallbackService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final ConcurrentMap<UUID, IRpcCallbackHandler> handlersRegistry = new ConcurrentHashMap<>();

    InetSocketAddress callbackServerAddress;

    public RpcCallbackService(InetSocketAddress callbackServerAddress) {
        this.callbackServerAddress = callbackServerAddress;
    }

    @Override
    public void register(UUID requestId, IRpcCallbackHandler rpcCallbackHandler) {
        log.info("registering callback handler for: " + requestId);
        handlersRegistry.put(requestId, rpcCallbackHandler);
    }

    @Override
    public void setResult(UUID requestId, Object result) {
        log.info("callback received for: " + requestId);
        IRpcCallbackHandler callbackHandler = handlersRegistry.remove(requestId);
        if (callbackHandler == null) {
            log.info("no callback handler was found, result will be dropped. Request ID: " + requestId);
            return;
        }
        callbackHandler.complete(result);
    }

    @Override
    public InetSocketAddress getCallbackServerAddress() {
        return callbackServerAddress;
    }
}
