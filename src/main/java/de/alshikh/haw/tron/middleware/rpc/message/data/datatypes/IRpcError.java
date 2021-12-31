package de.alshikh.haw.tron.middleware.rpc.message.data.datatypes;

public interface IRpcError {
    int getCode();
    String getMessage();
}
