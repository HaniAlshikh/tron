package de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes;

public interface IRpcCallbackHandler {
    Object getResult();

    void complete(Object result);
}
