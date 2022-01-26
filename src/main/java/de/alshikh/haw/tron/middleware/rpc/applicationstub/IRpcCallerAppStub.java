package de.alshikh.haw.tron.middleware.rpc.applicationstub;

import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;

import java.util.UUID;

public interface IRpcCallerAppStub {
    IRpcClientStub getRpcClientStub();

    UUID getServiceId();
}
