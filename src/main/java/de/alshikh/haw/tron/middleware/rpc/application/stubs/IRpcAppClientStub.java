package de.alshikh.haw.tron.middleware.rpc.application.stubs;

import de.alshikh.haw.tron.middleware.rpc.client.IRPCClient;

import java.util.UUID;

public interface IRpcAppClientStub {
    IRPCClient getRpcClient();

    UUID getServiceId();
}
