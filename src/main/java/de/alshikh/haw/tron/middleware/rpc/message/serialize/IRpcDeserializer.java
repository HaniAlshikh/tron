package de.alshikh.haw.tron.middleware.rpc.message.serialize;

public interface IRpcDeserializer {
    Object deserialize(Object obj, Class<?> type);
}
