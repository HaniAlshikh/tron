package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcServiceClientStub;
import de.alshikh.haw.tron.middleware.rpc.client.IRPCClient;

import java.lang.reflect.Method;

public class HelloWorldClient implements IHelloWorld, IRpcServiceClientStub {
    IRPCClient client;

    public HelloWorldClient(IRPCClient client) {
        this.client = client;
    }

    @Override
    public HelloWorldMessage helloWorld() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        Object response = client.invokeWithResponse(method);
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public HelloWorldMessage helloWorld(HelloWorldMessage message) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        Object response = client.invokeWithResponse(method, message);
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }
}
