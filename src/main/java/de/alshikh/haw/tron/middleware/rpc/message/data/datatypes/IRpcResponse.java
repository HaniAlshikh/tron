package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

public interface IRpcResponse {
    byte[] getBytes();

    boolean success();

    Object getResult();
}
