package de.alshikh.haw.tron.middleware.rpc.client;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public interface IRPCClient {
    @SuppressWarnings("unchecked")
    static <T> T getServiceStub(final Class<?> serviceInterface, final InetSocketAddress serverAddress) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new ClientStub(serviceInterface, serverAddress)
        );
    }
}
