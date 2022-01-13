package de.alshikh.haw.tron.middleware.rpc.callback.service;

import de.alshikh.haw.tron.middleware.directoryserver.service.IDirectoryService;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RpcCallbackService implements IRpcCallbackService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    private final ConcurrentMap<UUID, IRpcCallbackHandler> responseRegistry;
    // TODO: is this the right place? or should we send the port through the messaging protocol
    private IDirectoryService directoryService;


    private static final RpcCallbackService instance = new RpcCallbackService();
    public static RpcCallbackService getInstance() {
        return instance;
    }
    private RpcCallbackService() {
        responseRegistry = new ConcurrentHashMap<>();
    }

    // TODO maybe create a callback datatype?
    @Override
    public void register(UUID requestId, IRpcCallbackHandler callback) {
        responseRegistry.put(requestId, callback);
    }

    @Override
    public void setResponse(UUID requestId, Object response) {
        responseRegistry.get(requestId).complete(response);
    }

    public IDirectoryService getDirectoryService() {
        return directoryService;
    }

    public void setDirectoryService(IDirectoryService directoryService) {
        this.directoryService = directoryService;
    }
}
