package de.alshikh.haw.tron.middleware.rpc.common.data.exceptions;

public class MethodNotFoundRpcException extends RpcException{
    public MethodNotFoundRpcException() {
        this("method not found");
    }

    public MethodNotFoundRpcException(String message) {
        super(message);
        this.code = 32601;
    }
}
