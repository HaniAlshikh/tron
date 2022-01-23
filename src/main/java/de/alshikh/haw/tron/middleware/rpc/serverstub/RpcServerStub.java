package de.alshikh.haw.tron.middleware.rpc.serverstub;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.IRpcReceiver;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.RpcReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class RpcServerStub implements IRPCServerStub {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final HashMap<UUID, IRpcAppServerStub> serviceRegistry;
    private final IRpcReceiver rpcReceiver;

    public RpcServerStub(IRpcMessageApi rpcMessageApi) {
        this.serviceRegistry = new HashMap<>();
        this.rpcReceiver = new RpcReceiver(rpcMessageApi, serviceRegistry);
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
    public IRpcReceiver getRpcReceiver() {
        return rpcReceiver;
    }
}
