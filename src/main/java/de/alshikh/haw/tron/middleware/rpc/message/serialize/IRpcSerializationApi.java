package de.alshikh.haw.tron.middleware.rpc.message.serialize;

import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;

public interface IRpcSerializationApi extends IRpcSerializer, IRpcDeserializer {
    void setR(IRpcCallbackService rpcCallbackService);
}
