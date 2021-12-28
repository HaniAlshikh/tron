package de.alshikh.haw.tron.middleware.rpc.message.data.exceptions;

public class InvalidParamsRpcException extends RpcException{
    public InvalidParamsRpcException() {
        this("invalid parameters");
    }

    public InvalidParamsRpcException(String message) {
        super(message);
        this.code = 32602;
    }
}
