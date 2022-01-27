package de.alshikh.haw.tron.middleware.rpc.message.json.serialize;

import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializationApi;

public class JsonRpcSerializationApi implements IRpcSerializationApi {
    @Override
    public Object serialize(Object obj) {
        return obj;
    }

    @Override
    public Object deserialize(Object obj, Class<?> type) {
        return obj;
    }
}
