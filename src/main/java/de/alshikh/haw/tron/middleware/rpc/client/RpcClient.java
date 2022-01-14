package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMarshaller;
import de.alshikh.haw.tron.middleware.rpc.network.RpcConnection;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

// TODO: auto generate stubs
public class RpcClient implements IRPCClient {
    private final InetSocketAddress serverAddress;
    private final IRpcMarshaller rpcMarshaller;

    public RpcClient(InetSocketAddress serverAddress, IRpcMarshaller rpcMarshaller) {
        this.serverAddress = serverAddress;
        this.rpcMarshaller = rpcMarshaller;
    }

    @Override
    public void invoke(UUID serviceId, Method method, Object... args) {
        invoke(serviceId, null, method, args);
    }

    @Override
    public void invoke(UUID serviceId, IRpcCallbackHandler rpcCallbackHandler, Method method, Object... args) {
        ClientStub clientStub = new ClientStub(rpcMarshaller, new RpcConnection(serverAddress));
        clientStub.invoke(serviceId, rpcCallbackHandler, method, args);
    }

    @Override
    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }
}
