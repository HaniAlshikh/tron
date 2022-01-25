package de.alshikh.haw.tron.middleware.rpc.serverstub.unmarshal.data.execptions;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;

public class InvalidParamsRpcException extends RpcException {
    public InvalidParamsRpcException() {
        this("invalid parameters");
    }

    public InvalidParamsRpcException(String message) {
        super(message);
        this.code = 32602;
    }
}
