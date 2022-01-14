package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRPCClient {
    void invoke(UUID serviceId, Method method, Object... args);

    void invoke(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, Method method, Object... args);

    InetSocketAddress getServerAddress();
}
