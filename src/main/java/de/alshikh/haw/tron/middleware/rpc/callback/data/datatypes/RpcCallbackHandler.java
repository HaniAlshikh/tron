package de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes;

import java.util.concurrent.CompletableFuture;

public class RpcCallbackHandler implements IRpcCallbackHandler {

    private final CompletableFuture<Object> result;

    public RpcCallbackHandler() {
        this.result = new CompletableFuture<>();
    }

    @Override
    public Object getResult() {
        try {
            return result.get();
        } catch (Exception e) {
            return e; // TODO
        }
    }

    @Override
    public void complete(Object result) {
        this.result.complete(result);
    }
}
