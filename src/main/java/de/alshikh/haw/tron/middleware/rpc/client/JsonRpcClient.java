package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

// TODO: auto generate stubs
public class JsonRpcClient implements IRPCClient {

    private final ClientStub clientStub;

    public JsonRpcClient(InetSocketAddress serverAddress) {
        this.clientStub = new ClientStub(serverAddress, new JsonRpcMessageApi());
    }

    public JsonRpcClient(InetSocketAddress serverAddress, JsonRpcSerializer jsonRpcSerializer) {
        this.clientStub = new ClientStub(serverAddress, new JsonRpcMessageApi(jsonRpcSerializer));
    }

    @Override
    public Object invoke(UUID serviceId, Method method, Object... args) {
        clientStub.setWaitForResponse(false);
        return clientStub.invoke(serviceId, method, args);
    }

    @Override
    public Object invokeWithResponse(UUID serviceId, Method method, Object... args) {
        clientStub.setWaitForResponse(true);
        return clientStub.invoke(serviceId, method, args);
    }
}
