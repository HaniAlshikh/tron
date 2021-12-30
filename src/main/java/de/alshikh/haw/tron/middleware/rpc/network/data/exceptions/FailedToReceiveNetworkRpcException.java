package de.alshikh.haw.tron.middleware.rpc.network.data.exceptions;

import de.alshikh.haw.tron.middleware.rpc.common.data.exceptions.RpcException;

import java.net.Socket;

public class FailedToReceiveNetworkRpcException extends RpcException {
    public static String MESSAGE = "Failed to receive data due to network error";

    public FailedToReceiveNetworkRpcException() {
        this(MESSAGE);
    }

    public FailedToReceiveNetworkRpcException(Socket connection) {
        this(MESSAGE + " from" + connection.getInetAddress() + ":" + connection.getPort());
    }

    public FailedToReceiveNetworkRpcException(String message) {
        super(message);
        this.code = 32281;
    }
}
