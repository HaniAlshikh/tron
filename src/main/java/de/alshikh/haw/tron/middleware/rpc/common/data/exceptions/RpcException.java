package de.alshikh.haw.tron.middleware.rpc.common.data.exceptions;

import de.alshikh.haw.tron.middleware.rpc.message.data.datatypes.IRpcError;

public class RpcException extends Exception implements IRpcError {

    protected int code = -1;

    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    @Override
    public int getCode() {
        return code;
    }
}
