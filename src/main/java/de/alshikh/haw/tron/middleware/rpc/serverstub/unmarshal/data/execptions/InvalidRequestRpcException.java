package de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;

public class InvalidRequestRpcException extends RpcException {
    public InvalidRequestRpcException() {
        this("Invalid Request");
    }

    public InvalidRequestRpcException(String message) {
        super(message);
        this.code = 32600;
    }
}
