package de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.callback.service.RpcCallbackService;

import java.util.UUID;

public class RpcCallback implements IRpcCallback {
    // TODO: singleton days are gone -> find a better way
    private final IRpcCallbackService rpcCallbackService = RpcCallbackService.getInstance();

    public RpcCallback() {}

    @Override
    public void retrn(UUID requestId, Object result) {
        rpcCallbackService.setResponse(requestId, result);
    }
}
