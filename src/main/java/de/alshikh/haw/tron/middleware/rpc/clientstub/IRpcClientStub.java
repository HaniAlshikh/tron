package de.alshikh.haw.tron.middleware.rpc.clientstub;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRpcClientStub {
    void invoke(UUID serviceId, Method method, Object... args);

    void invoke(UUID serviceId, boolean isBestEffort, Method method, Object... args);

    void invoke(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, Method method, Object... args);

    void invoke(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, boolean isBestEffort, Method method, Object... args);

    InetSocketAddress getServerAddress();
}
