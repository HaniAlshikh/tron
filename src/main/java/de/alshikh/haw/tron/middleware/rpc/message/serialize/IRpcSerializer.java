package de.alshikh.haw.tron.middleware.rpc.message.serialize;

public interface IRpcSerializer {
    Object deserialize(Object obj, Class<?> type);

    Object serialize(Object obj);
}
