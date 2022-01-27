package de.alshikh.haw.tron.middleware.rpc.serverstub;

import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCalleeAppStub;
import de.alshikh.haw.tron.middleware.rpc.serverstub.receive.IRpcReceiver;

import java.util.UUID;

public interface IRpcServerStub {
    void register(IRpcCalleeAppStub rpcCalleeAppStub);

    void unregister(UUID id);

    IRpcReceiver getRpcReceiver();
}
