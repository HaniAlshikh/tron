package de.alshikh.haw.tron.middleware.rpc.serverstub.data.exceptions;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;

public class ServiceNotFoundRpcException extends RpcException {
    public static final String MESSAGE = "service not found";

    public ServiceNotFoundRpcException() {
        this(MESSAGE);
    }

    public ServiceNotFoundRpcException(Class<?> clazz) {
        this(MESSAGE + ": " + clazz.getSimpleName());
    }

    public ServiceNotFoundRpcException(String message) {
        super(message);
        this.code = 32100;
    }
}
