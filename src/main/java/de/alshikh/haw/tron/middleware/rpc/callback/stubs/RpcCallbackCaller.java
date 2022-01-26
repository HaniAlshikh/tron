package de.alshikh.haw.tron.middleware.rpc.callback.stubs;

import de.alshikh.haw.tron.middleware.rpc.applicationstub.IRpcCallerAppStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRpcClientStub;

import java.lang.reflect.Method;
import java.util.UUID;

public class RpcCallbackCaller implements IRpcCallback, IRpcCallerAppStub {
    public static UUID serviceId = UUID.fromString("08fd9cc9-a1fd-454e-ae21-f3c1329ab93c");

    IRpcClientStub rpcClientStub;

    public RpcCallbackCaller(IRpcClientStub rpcClientStub) {
        this.rpcClientStub = rpcClientStub;
    }

    @Override
    public void retrn(UUID requestId, Object result) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClientStub.invoke(serviceId, method, requestId, result);
    }

    @Override
    public IRpcClientStub getRpcClientStub() {
        return rpcClientStub;
    }

    @Override
    public UUID getServiceId() {
        return serviceId;
    }
}
