package de.alshikh.haw.tron.middleware.rpc.client;

import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcMessage;
import de.alshikh.haw.tron.middleware.rpc.message.json.JsonRpcSerializer;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

// TODO: auto generate stubs
public class JsonRpcClient implements IRPCClient {

    private final ClientStub clientStub;

    public JsonRpcClient(Class<?> serviceInterface, InetSocketAddress serverAddress) {
        this.clientStub = new ClientStub(serviceInterface, serverAddress, new JsonRpcMessage());
    }

    public JsonRpcClient(Class<?> serviceInterface, InetSocketAddress serverAddress, JsonRpcSerializer jsonRpcSerializer) {
        this.clientStub = new ClientStub(serviceInterface, serverAddress, new JsonRpcMessage(jsonRpcSerializer));
    }

    @Override
    public Object invoke(Method method, Object... args) {
        clientStub.setWaitForResponse(false);
        return clientStub.invoke(method, args);
    }

    @Override
    public Object invokeWithResponse(Method method, Object... args) {
        clientStub.setWaitForResponse(true);
        return clientStub.invoke(method, args);
    }
}
