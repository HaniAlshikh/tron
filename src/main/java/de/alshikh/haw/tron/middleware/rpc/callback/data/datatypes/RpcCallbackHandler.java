package de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes;

import de.alshikh.haw.tron.manager.Config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RpcCallbackHandler implements IRpcCallbackHandler {

    private final CompletableFuture<Object> result = new CompletableFuture<>();;

    public RpcCallbackHandler() {}

    @Override
    public Object getResult() {
        try {
            return result.get(Config.RPC_CALLBACK_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    public void complete(Object result) {
        this.result.complete(result);
    }
}
