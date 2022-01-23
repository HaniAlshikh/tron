package de.alshikh.haw.tron.middleware.rpc.callback.stubs;

import de.alshikh.haw.tron.middleware.rpc.application.stubs.IRpcAppClientStub;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.clientstub.IRPCClientStub;

import java.lang.reflect.Method;
import java.util.UUID;

// TODO: maybe create stubs for IRpcCallback?
public class RpcCallbackClient implements IRpcCallback, IRpcAppClientStub {
    public static UUID id = UUID.fromString("08fd9cc9-a1fd-454e-ae21-f3c1329ab93c");

    IRPCClientStub rpcClient;

    public RpcCallbackClient(IRPCClientStub rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public void retrn(UUID requestId, Object result) {
        Method method = new Object(){}.getClass().getEnclosingMethod();
        rpcClient.invoke(id, method, requestId, result);
    }

    @Override
    public IRPCClientStub getRpcClient() {
        return rpcClient;
    }

    @Override
    public UUID getServiceId() {
        return id;
    }
}
