package de.alshikh.haw.tron.middleware.rpc.message.json.serialize;

import de.alshikh.haw.tron.middleware.rpc.message.serialize.IRpcSerializer;

public class JsonRpcSerializer implements IRpcSerializer {

    @Override
    public Object deserialize(Object obj, Class<?> type) {
        return obj;
    }

    @Override
    public Object serialize(Object obj) {
        return obj;
    }
}
