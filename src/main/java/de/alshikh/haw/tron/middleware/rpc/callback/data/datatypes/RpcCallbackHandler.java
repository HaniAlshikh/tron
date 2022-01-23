package de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RpcCallbackHandler implements IRpcCallbackHandler {

    private final CompletableFuture<Object> result;

    public RpcCallbackHandler() {
        this.result = new CompletableFuture<>();
    }

    @Override
    public Object getResult() {
        try {
            return result.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            return e; // TODO
        }
    }

    @Override
    public void complete(Object result) {
        this.result.complete(result);
    }
}
