package de.alshikh.haw.tron.middleware.rpc.message.serialize;

public interface IRpcSerializer {
    Object serialize(Object obj);

    Object deserialize(Object obj, Class<?> type);
}
