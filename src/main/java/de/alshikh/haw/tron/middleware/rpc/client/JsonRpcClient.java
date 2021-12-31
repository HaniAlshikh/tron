package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

// TODO: auto generate stubs
public class JsonRpcClient implements IRPCClient {

    private final SocketAddress serverAddress;
    private final JsonRpcMessageApi jsonRpcMessageApi;

    public JsonRpcClient(InetSocketAddress serverAddress) {
        this(serverAddress, new JsonRpcMessageApi());
    }

    public JsonRpcClient(InetSocketAddress serverAddress, JsonRpcSerializer jsonRpcSerializer) {
        this(serverAddress,  new JsonRpcMessageApi(jsonRpcSerializer));
    }

    public JsonRpcClient(InetSocketAddress serverAddress, JsonRpcMessageApi jsonRpcMessageApi) {
        this.serverAddress = serverAddress;
        this.jsonRpcMessageApi = jsonRpcMessageApi;
    }

    @Override
    public Object invoke(UUID serviceId, Method method, Object... args) {
        ClientStub clientStub = new ClientStub(serverAddress, jsonRpcMessageApi);
        return clientStub.invoke(serviceId, method, args);
    }

    @Override
    public Object invokeWithResponse(UUID serviceId, Method method, Object... args) {
        ClientStub clientStub = new ClientStub(serverAddress, jsonRpcMessageApi, true);
        return clientStub.invoke(serviceId, method, args);
    }
}
