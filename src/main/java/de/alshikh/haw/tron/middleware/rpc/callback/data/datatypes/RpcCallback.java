package de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes;

import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;

import java.util.UUID;

public class RpcCallback implements IRpcCallback {
    private final IRpcCallbackService rpcCallbackService;

    public RpcCallback(IRpcCallbackService rpcCallbackService) {
        this.rpcCallbackService = rpcCallbackService;
    }

    @Override
    public void retrn(UUID requestId, Object result) {
        rpcCallbackService.setResult(requestId, result);
    }
}
