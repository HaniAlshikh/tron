package de.alshikh.haw.tron.middleware.rpc.message;

public interface IRpcResponse {
    byte[] getBytes();

    boolean success();

    Object result();
}
