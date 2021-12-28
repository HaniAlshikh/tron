package de.alshikh.haw.tron.middleware.rpc.server;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcServiceServerStub;

public interface IRPCServer {

    void register(Class<?> serviceInterface, IRpcServiceServerStub serviceServerStub);

    void start();

    void stop();

    boolean isRunning();

    int getPort();
}