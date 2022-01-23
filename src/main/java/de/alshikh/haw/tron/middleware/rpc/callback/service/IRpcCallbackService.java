package de.alshikh.haw.tron.middleware.rpc.callback.service;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;

import java.net.InetSocketAddress;
import java.util.UUID;

public interface IRpcCallbackService {
    void register(UUID requestId, IRpcCallbackHandler callback);

    void setResponse(UUID requestId, Object response);

    InetSocketAddress getCallbackServerAddress();
}
