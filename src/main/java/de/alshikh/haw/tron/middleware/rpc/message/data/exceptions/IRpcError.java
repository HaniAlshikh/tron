package de.alshikh.haw.tron.middleware.rpc.message.data.exceptions;

public interface IRpcError {
    int getCode();
    String getMessage();
}
