package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.client.IRPCClient;

import java.lang.reflect.Method;
import java.util.UUID;

public class HelloWorldClient implements IHelloWorld, IRpcAppClientStub {
    public static UUID id = UUID.fromString("08fd9cc9-a1ff-454e-ae22-f3c1329ab93c");

    IRPCClient rpcClient;

    public HelloWorldClient(IRPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public HelloWorldMessage helloWorld() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        Object response = rpcClient.invokeWithResponse(id, method);
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public HelloWorldMessage helloWorld(HelloWorldMessage message) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        Object response = rpcClient.invokeWithResponse(id, method, message);
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
