package de.alshikh.haw.tron.middleware.rpc.common.data.exceptions;

public class ParseErrorRpcException extends RpcException{
    public ParseErrorRpcException() {
        this("Parse error");
    }

    public ParseErrorRpcException(String message) {
        super(message);
        this.code = 32700;
    }
}
