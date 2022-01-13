package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessageApi;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;

// TODO: auto generate stubs
public class JsonRpcClient implements IRPCClient {
    private final InetSocketAddress serverAddress;
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
        clientStub.invoke(serviceId, null, method, args);
        return null; // TODO
    }

    // TODO: implement this correctly with rpc callback
    @Override
    public Object invokeWithResponse(UUID serviceId, Method method, Object... args) {
        ClientStub clientStub = new ClientStub(serverAddress, jsonRpcMessageApi, true);
        IRpcCallbackHandler callbackHandler = new RpcCallbackHandler(); // TODO: should this be moved to AppStub? (A callback is a user-defined function)
        clientStub.invoke(serviceId, callbackHandler, method, args);
        return callbackHandler.getResult();
    }

    @Override
    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }
}
