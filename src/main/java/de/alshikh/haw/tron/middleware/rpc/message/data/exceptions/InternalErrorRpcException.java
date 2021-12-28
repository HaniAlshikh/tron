package de.alshikh.haw.tron.middleware.rpc.message.data.exceptions;

public class InternalErrorRpcException extends RpcException{
    public InternalErrorRpcException() {
        this("Internal error");
    }

    public InternalErrorRpcException(String message) {
        super(message);
        this.code = 32603;
    }
}
