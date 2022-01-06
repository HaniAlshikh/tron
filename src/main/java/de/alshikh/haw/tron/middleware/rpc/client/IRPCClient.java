package de.alshikh.haw.tron.middleware.rpc.client;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRPCClient {
    Object invoke(UUID serviceId, Method method, Object... args);

    Object invokeWithResponse(UUID serviceId, Method method, Object... args);

    InetSocketAddress getServerAddress();
}
