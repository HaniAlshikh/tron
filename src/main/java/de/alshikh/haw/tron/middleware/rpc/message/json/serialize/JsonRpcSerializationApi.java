package de.alshikh.haw.tron.middleware.rpc.message.json.serialize;

import de.alshikh.haw.tron.middleware.rpc.callback.service.IRpcCallbackService;
import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializationApi;

public class JsonRpcSerializationApi implements IRpcSerializationApi {
    protected IRpcCallbackService rcs;

    @Override
    public Object serialize(Object obj) {
        return obj;
    }

    @Override
    public Object deserialize(Object obj, Class<?> type) {
        return obj;
    }

    @Override
    public void setR(IRpcCallbackService rpcCallbackService) {
        rcs = rpcCallbackService;
    }
}
