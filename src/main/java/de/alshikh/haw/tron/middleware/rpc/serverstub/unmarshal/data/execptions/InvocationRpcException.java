package de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;

import java.lang.reflect.Method;

public class InvocationRpcException extends RpcException {
    public static final String MESSAGE = "there was an error invoking the method";

    public InvocationRpcException() {
        this(MESSAGE);
    }

    public InvocationRpcException(Method method) {
        this(MESSAGE + ": " + method.getName());
    }

    public InvocationRpcException(String message) {
        super(message);
        this.code = 32100;
    }
}
