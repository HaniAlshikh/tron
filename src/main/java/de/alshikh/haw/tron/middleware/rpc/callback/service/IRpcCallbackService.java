package de.alshikh.haw.tron.middleware.rpc.callback.service;

import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallback;
import de.alshikh.haw.tron.middleware.rpc.callback.data.datatypes.IRpcCallbackHandler;
import de.alshikh.haw.tron.middleware.rpc.message.IRpcMarshaller;

import java.net.InetAddress;
import java.util.UUID;

public interface IRpcCallbackService {
    void register(UUID requestId, IRpcCallbackHandler callback);

    void setResponse(UUID requestId, Object response);

    IRpcCallback newRpcCallback(InetAddress receiverAddress, int port);

    IRpcMarshaller getRpcMarshaller();

    void setRpcMarshaller(IRpcMarshaller rpcMarshaller);
}
