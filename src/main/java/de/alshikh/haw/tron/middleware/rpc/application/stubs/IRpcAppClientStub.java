package de.alshikh.haw.tron.middleware.rpc.application.stubs;

import de.alshikh.haw.tron.middleware.rpc.clientstub.IRPCClientStub;

import java.util.UUID;

public interface IRpcAppClientStub {
    IRPCClientStub getRpcClient();

    UUID getServiceId();
}
