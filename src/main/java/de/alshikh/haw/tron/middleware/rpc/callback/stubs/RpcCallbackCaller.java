package de.alshikh.haw.tron.middleware.rpc.callback.stubs;

import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;

import java.lang.reflect.Method;
import java.util.UUID;

public class RpcCallbackCaller implements IRpcCallback, IRpcCallerAppStub {
    public static UUID SERVICE_ID = RpcCallbackCallee.SERVICE_ID;

    IRpcClientStub rpcClientStub;

    public RpcCallbackCaller(IRpcClientStub rpcClientStub) {
        this.rpcClientStub = rpcClientStub;
    }

    @Override
    public void retrn(UUID requestId, Object result) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(SERVICE_ID, method, requestId, result);
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
