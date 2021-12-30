package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppServerStub;

public interface IRPCServer {

    void register(IRpcAppServerStub serviceServerStub);

    void start();

    void stop();

    boolean isRunning();

    int getPort();
}
