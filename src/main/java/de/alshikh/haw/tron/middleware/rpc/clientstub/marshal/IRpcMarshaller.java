package de.alshikh.haw.tron.middleware.rpc.clientstub.marshal;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRpcMarshaller {
    void marshal(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, boolean bestEffort, Method method, Object[] args);

    InetSocketAddress getServerAddress();
}
