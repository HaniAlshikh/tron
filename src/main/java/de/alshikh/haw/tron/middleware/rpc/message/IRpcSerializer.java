package de.alshikh.haw.tron.middleware.rpc.message;

public interface IRpcSerializer {
    Object deserialize(Object obj, Class<?> type);

    Object serialize(Object obj);
}
