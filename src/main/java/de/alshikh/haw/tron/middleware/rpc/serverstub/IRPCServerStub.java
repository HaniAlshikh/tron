package de.alshikh.haw.tron.middleware.rpc.serverstub;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.IRpcReceiver;

import java.util.UUID;

public interface IRPCServerStub {
    void register(IRpcAppServerStub serviceServerStub);

    void unregister(UUID id);

    IRpcReceiver getRpcReceiver();
}
