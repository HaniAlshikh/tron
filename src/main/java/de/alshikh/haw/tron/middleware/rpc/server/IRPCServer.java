package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMessageApi;

import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRPCServer {

    void register(IRpcAppServerStub serviceServerStub);

    void unregister(UUID id);

    void start();

    void stop();

    boolean isRunning();

    InetSocketAddress getSocketAddress();

    IRpcMessageApi getMessageApi();
}
