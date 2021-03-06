package de.alshikh.haw.tron.middleware.helloworld;

import de.alshikh.haw.tron.middleware.helloworld.service.IHelloWorld;
import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;
import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.RpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;

import java.lang.reflect.Method;
import java.util.UUID;

public class HelloWorldCaller implements IHelloWorld, IRpcCallerAppStub {
    public static UUID SERVICE_ID = HelloWorldCallee.SERVICE_ID;

    IRpcClientStub rpcClientStub;

    public HelloWorldCaller(IRpcClientStub rpcClientStub) {
        this.rpcClientStub = rpcClientStub;
    }

    @Override
    public HelloWorldMessage helloWorld() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClientStub.invoke(SERVICE_ID, rpcCallbackHandler, method);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public HelloWorldMessage helloWorldBestEffort() {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClientStub.invoke(SERVICE_ID, rpcCallbackHandler, true, method);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public HelloWorldMessage helloWorld(HelloWorldMessage message) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        IRpcCallbackHandler rpcCallbackHandler = new RpcCallbackHandler();
        rpcClientStub.invoke(SERVICE_ID, rpcCallbackHandler, method, message);
        Object response = rpcCallbackHandler.getResult();
        if (response instanceof Exception)
            return null;
        return (HelloWorldMessage) response;
    }

    @Override
    public IRpcClientStub getRpcClientStub() {
        return rpcClientStub;
    }

    @Override
    public UUID getServiceId() {
        return SERVICE_ID;
    }
}
