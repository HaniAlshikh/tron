package de.alshikh.haw.tron.middleware.rpc.common.data.exceptions;

public class InvalidRequestRpcException extends RpcException{
    public InvalidRequestRpcException() {
        this("Invalid Request");
    }

    public InvalidRequestRpcException(String message) {
        super(message);
        this.code = 32600;
    }
}
