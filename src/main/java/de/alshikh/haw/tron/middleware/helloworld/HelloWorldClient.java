package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRPCClientStub;

import java.lang.reflect.Method;
import java.util.UUID;

public class HelloWorldClient implements IHelloWorld, IRpcAppClientStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1ff-454e-ae22-f3c1329ab93c");

    IRPCClientStub rpcClient;

    public HelloWorldClient(IRPCClientStub rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public HelloWorldMessage helloWorld() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClient.invoke(serviceId, rpcCallbackHandler, method);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public HelloWorldMessage helloWorldBestEffort() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClient.invoke(serviceId, rpcCallbackHandler, true, method);
        Object response = rpcCallbackHandler.getResult(); // TODO: bestEffort and callback?
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public HelloWorldMessage helloWorld(HelloWorldMessage message) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClient.invoke(serviceId, rpcCallbackHandler, method, message);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public IRPCClientStub getRpcClient() {
        return rpcClient;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
